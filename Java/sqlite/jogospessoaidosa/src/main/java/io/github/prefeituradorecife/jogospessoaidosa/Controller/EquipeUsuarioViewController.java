package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Representante;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Utils.PdfUtils;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/equipes/usuarios")
public class EquipeUsuarioViewController {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PdfUtils pdfUtils;

    @GetMapping("/{id}")
    public String listarUsuariosDaEquipe(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {
    Equipe equipe = equipeService.buscarPorId(id);

    Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
    Page<Pessoa> pageDisponiveis = pessoaService.buscarDisponiveis(null, id, pageable);

    // IDs dos representantes já vinculados
    List<Long> representantesSelecionados = equipe.getRepresentantes().stream()
        .map(rep -> rep.getPessoa() != null ? rep.getPessoa().getId() : null)
        .filter(Objects::nonNull)
        .toList();
    equipe.setRepresentantesIds(representantesSelecionados);
    // Adiciona atributos ao modelo
    model.addAttribute("equipe", equipe);
    model.addAttribute("pessoasDisponiveis", equipe.getRepresentantes().stream()
        .map(Representante::getPessoa)
        .filter(Objects::nonNull)
        .toList());
    model.addAttribute("RPA", RPA.values());

    // Paginação
    model.addAttribute("currentPage", pageDisponiveis.getNumber());
    model.addAttribute("totalPages", pageDisponiveis.getTotalPages());
    model.addAttribute("totalItems", pageDisponiveis.getTotalElements());
    model.addAttribute("pageSize", pageDisponiveis.getSize());

    return "equipesUsuarios";
}


@GetMapping("/{id}/buscar")
public String buscarUsuariosDaEquipe(@PathVariable Long id,
                                     @RequestParam(required = false) String filtro,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {
    Equipe equipe = equipeService.buscarPorId(id);

    List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
    List<Pessoa> pessoasParticipantes = pessoaService.listarParticipantesPorEquipe(id);

    Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
    Page<Pessoa> pageDisponiveis = pessoaService.buscarDisponiveis(filtro, null, pageable);

    List<Pessoa> pessoasBase;
    if (filtro != null && !filtro.trim().isEmpty()) {
        String termo = filtro.trim().toLowerCase();

        java.util.function.Predicate<Pessoa> matches = p -> {
            if (p == null) return false;
            if (p.getNome() != null && p.getNome().toLowerCase().contains(termo)) return true;
            if (p.getCpf() != null && p.getCpf().toLowerCase().contains(termo)) return true;
            if (p.getRg() != null && p.getRg().toLowerCase().contains(termo)) return true;
            if (p.getIdade() != null && String.valueOf(p.getIdade()).equals(termo)) return true;
            if ((termo.equalsIgnoreCase("sim") || termo.equals("1") || termo.equalsIgnoreCase("true") || termo.equalsIgnoreCase("idoso"))
                && Boolean.TRUE.equals(p.getIdoso())) return true;
            if ((termo.equalsIgnoreCase("nao") || termo.equalsIgnoreCase("não") || termo.equals("0") || termo.equalsIgnoreCase("false"))
                && Boolean.FALSE.equals(p.getIdoso())) return true;
            if (p.getDoencas() != null) {
                for (var d : p.getDoencas()) {
                    if (d != null && d.getNome() != null && d.getNome().toLowerCase().contains(termo)) return true;
                }
            }
            return false;
        };

        List<Pessoa> combined = new ArrayList<>();
        combined.addAll(pessoasNaEquipe.stream().filter(matches).toList());
        combined.addAll(pessoasParticipantes.stream().filter(matches).toList());
        combined.addAll(pageDisponiveis.getContent());

        Map<Long, Pessoa> byId = new LinkedHashMap<>();
        for (Pessoa p : combined) {
            if (p != null && p.getId() != null) {
                byId.putIfAbsent(p.getId(), p);
            }
        }
        pessoasBase = new ArrayList<>(byId.values());
    } else {
        pessoasBase = new ArrayList<>(pageDisponiveis.getContent());
    }

    // IDs dos representantes já vinculados
    List<Long> representantesSelecionados = equipe.getRepresentantes().stream()
        .map(r -> r.getPessoa() != null ? r.getPessoa().getId() : null)
        .filter(Objects::nonNull)
        .toList();

    // Garante que representantes estejam na base
    Set<Long> ids = pessoasBase.stream().map(Pessoa::getId).collect(Collectors.toSet());
    for (Representante rep : equipe.getRepresentantes()) {
        Pessoa p = rep.getPessoa();
        if (p != null && ids.add(p.getId())) {
            pessoasBase.add(p);
        }
    }

    model.addAttribute("equipe", equipe);
    model.addAttribute("pessoasNaEquipe", pessoasNaEquipe);
    model.addAttribute("pessoasDisponiveis", pageDisponiveis.getContent());
    model.addAttribute("pessoasParticipantes", pessoasParticipantes);
    model.addAttribute("pessoasBase", pessoasBase);
    model.addAttribute("representantesSelecionados", representantesSelecionados);
    model.addAttribute("filtro", filtro);
    model.addAttribute("RPA", RPA.values());

    model.addAttribute("currentPage", pageDisponiveis.getNumber());
    model.addAttribute("totalPages", pageDisponiveis.getTotalPages());
    model.addAttribute("totalItems", pageDisponiveis.getTotalElements());
    model.addAttribute("pageSize", pageDisponiveis.getSize());

    return "equipesUsuarios";
}

// ###############################################################################################################################
    @PostMapping("/adicionarUsuariosEquipe")
    public String adicionarUsuarios(@RequestParam Long equipeId,
                                    @RequestParam(required = false) List<Long> pessoaIds) {

        // Se não vier nada, busca todos os IDs disponíveis
        List<Long> idsParaAdicionar = (pessoaIds == null || pessoaIds.isEmpty())
                ? pessoaService.buscarIdsDisponiveis()
                : pessoaIds;

        // Só chama o serviço se houver IDs
        if (!idsParaAdicionar.isEmpty()) {
            pessoaService.adicionarEquipeParaPessoas(equipeId, idsParaAdicionar);
        }

        return "redirect:/equipes/usuarios/" + equipeId;
    }

    @PostMapping("/removerUsuariosEquipe")
    public String removerUsuarios(@RequestParam Long equipeId,
                                  @RequestParam(value = "pessoaIds", required = false) List<Long> pessoaIds,
                                  @RequestParam(value = "pessoaIds[]", required = false) List<Long> pessoaIdsArray) {
        List<Long> idsSelecionados = pessoaIds != null && !pessoaIds.isEmpty() ? pessoaIds : pessoaIdsArray;
        if (idsSelecionados != null && !idsSelecionados.isEmpty()) {
            pessoaService.removerEquipeDasPessoas(equipeId, idsSelecionados);
        }
        return "redirect:/equipes/usuarios/" + equipeId;
    }
    
    @PostMapping("/adicionarUsuariosParticipantes")
    public String adicionarUsuariosParticipantes(@RequestParam Long equipeId,
                                                 @RequestParam(value = "pessoaIds", required = false) List<Long> pessoaIds,
                                                 @RequestParam(value = "pessoaIds[]", required = false) List<Long> pessoaIdsArray) {
        List<Long> idsSelecionados = pessoaIds != null && !pessoaIds.isEmpty() ? pessoaIds : pessoaIdsArray;

        if (idsSelecionados != null && !idsSelecionados.isEmpty()) {
            equipeService.adicionarPessoasNaEquipe(equipeId, idsSelecionados);
        }

        return "redirect:/equipes/usuarios/" + equipeId;
    }

    @PostMapping("/removerUsuariosParticipantes")
    public String removerUsuariosParticipantes(@RequestParam Long equipeId,
                                               @RequestParam(value = "pessoaIds", required = false) List<Long> pessoaIds,
                                               @RequestParam(value = "pessoaIds[]", required = false) List<Long> pessoaIdsArray) {

        List<Long> idsSelecionados = pessoaIds != null && !pessoaIds.isEmpty() ? pessoaIds : pessoaIdsArray;
        if (idsSelecionados != null && !idsSelecionados.isEmpty()) {
            equipeService.removerPessoasDaEquipe(equipeId, idsSelecionados);
        }

        return "redirect:/equipes/usuarios/" + equipeId;
    }

// ###############################################################################################################################
    @GetMapping("/gerarPdfTodasAsPessoasDaEquipe/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipe(@PathVariable Long id,
                                                                             @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);

        List<Pessoa> representantesSelecionados = (representanteIds == null || representanteIds.isEmpty())
                ? List.of()
                : pessoaService.buscarPorIds(representanteIds);

        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);
        ByteArrayInputStream bis = pdfUtils.gerarPdf(pessoasNaEquipe, equipe, subtitulo.isBlank() ? null : subtitulo);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    @GetMapping("/gerarPdfTodasAsPessoasDaEquipeIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipeIdosos(@PathVariable Long id,
                                                                                  @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
        List<Pessoa> idososNaEquipe = pessoasNaEquipe.stream()
                .filter(p -> p.getIdade() >= 60)
                .toList();

        List<Pessoa> representantesSelecionados = (representanteIds == null || representanteIds.isEmpty())
                ? List.of()
                : pessoaService.buscarPorIds(representanteIds);

        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);
        ByteArrayInputStream bis = pdfUtils.gerarPdf(idososNaEquipe, equipe, subtitulo.isBlank() ? null : subtitulo);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=idosos-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/gerarPdfTodasAsPessoasParticipantes/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantes(@PathVariable Long id,
                                                                                 @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> participantes = pessoaService.listarParticipantesPorEquipe(id);
        List<Pessoa> representantesSelecionados = (representanteIds == null || representanteIds.isEmpty())
                ? List.of()
                : pessoaService.buscarPorIds(representanteIds);

        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);
        ByteArrayInputStream bis = pdfUtils.gerarPdf(participantes, equipe, subtitulo.isBlank() ? null : subtitulo);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=participantes-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    @GetMapping("/gerarPdfTodasAsPessoasParticipantesIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantesIdosos(@PathVariable Long id,
                                                                                       @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> participantes = pessoaService.listarParticipantesPorEquipe(id);
        List<Pessoa> idososParticipantes = participantes.stream()
                .filter(p -> p.getIdade() >= 60)
                .toList();

        List<Pessoa> representantesSelecionados = (representanteIds == null || representanteIds.isEmpty())
                ? List.of()
                : pessoaService.buscarPorIds(representanteIds);

        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);
        ByteArrayInputStream bis = pdfUtils.gerarPdf(idososParticipantes, equipe, subtitulo.isBlank() ? null : subtitulo);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=participantes-idosos-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


}
