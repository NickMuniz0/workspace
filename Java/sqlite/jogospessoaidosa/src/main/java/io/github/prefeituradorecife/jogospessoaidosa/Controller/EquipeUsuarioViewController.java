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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/equipes/usuarios")
public class EquipeUsuarioViewController {

    private static final String VIEW_EQUIPE_USUARIOS = "equipesUsuarios";

    private final EquipeService equipeService;
    private final PessoaService pessoaService;
    private final PdfUtils pdfUtils;

    public EquipeUsuarioViewController(EquipeService equipeService, PessoaService pessoaService, PdfUtils pdfUtils) {
        this.equipeService = equipeService;
        this.pessoaService = pessoaService;
        this.pdfUtils = pdfUtils;
    }

    @GetMapping("/{id}")
    public String listarUsuariosDaEquipe(@PathVariable Long id,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     Model model) {
        Equipe equipe = equipeService.buscarPorId(id);

        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").ascending());
        Page<Pessoa> pageDisponiveis = pessoaService.buscarDisponiveis(null, id, pageable);
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
        List<Pessoa> pessoasParticipantes = pessoaService.listarParticipantesPorEquipe(id);

        List<Long> representantesSelecionados = equipe.getRepresentantes().stream()
                .map(rep -> rep.getPessoa() != null ? rep.getPessoa().getId() : null)
                .filter(Objects::nonNull)
                .toList();
        equipe.setRepresentantesIds(representantesSelecionados);

        model.addAttribute("equipe", equipe);
        model.addAttribute("pessoasDisponiveis", equipe.getRepresentantes().stream()
                .map(Representante::getPessoa)
                .filter(Objects::nonNull)
                .toList());
        model.addAttribute("RPA", RPA.values());
        model.addAttribute("pessoasNaEquipe", pessoasNaEquipe);
        model.addAttribute("pessoasParticipantes", pessoasParticipantes);
        model.addAttribute("currentPage", pageDisponiveis.getNumber());
        model.addAttribute("totalPages", pageDisponiveis.getTotalPages());
        model.addAttribute("totalItems", pageDisponiveis.getTotalElements());
        model.addAttribute("pageSize", pageDisponiveis.getSize());

        return VIEW_EQUIPE_USUARIOS;
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
        List<Pessoa> pessoasBase = montarListaBase(filtro, pessoasNaEquipe, pessoasParticipantes, pageDisponiveis);

        List<Long> representantesSelecionados = equipe.getRepresentantes().stream()
                .map(r -> r.getPessoa() != null ? r.getPessoa().getId() : null)
                .filter(Objects::nonNull)
                .toList();

        Set<Long> ids = pessoasBase.stream().map(Pessoa::getId).collect(Collectors.toSet());
        for (Representante rep : equipe.getRepresentantes()) {
            Pessoa pessoa = rep.getPessoa();
            if (pessoa != null && ids.add(pessoa.getId())) {
                pessoasBase.add(pessoa);
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

        return VIEW_EQUIPE_USUARIOS;
    }

    @PostMapping("/adicionarUsuariosEquipe")
    public String adicionarUsuarios(@RequestParam Long equipeId,
                                    @RequestParam(required = false) List<Long> pessoaIds) {
        List<Long> idsParaAdicionar = (pessoaIds == null || pessoaIds.isEmpty())
                ? pessoaService.buscarIdsDisponiveis()
                : pessoaIds;

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
                                    @RequestParam(value = "pessoaIds", required = false) List<Long> pessoaIds) {
        List<Long> idsParaAdicionar = (pessoaIds == null || pessoaIds.isEmpty())
                ? pessoaService.buscarIdsDisponiveis()
                : pessoaIds;
        if (idsParaAdicionar != null && !idsParaAdicionar.isEmpty()) {
            equipeService.adicionarPessoasNaEquipe(equipeId, idsParaAdicionar);
        }

        return "redirect:/equipes/usuarios/" + equipeId;
    }

    @PostMapping("/removerUsuariosParticipantes")
    public String removerUsuariosParticipantes(@RequestParam Long equipeId,
                                               @RequestParam(value = "pessoaIds", required = false) List<Long> pessoaIds) {
        List<Long> idsParaRemover = (pessoaIds == null || pessoaIds.isEmpty())
                ? pessoaService.buscarIdsDisponiveis()
                : pessoaIds;
        if (idsParaRemover != null && !idsParaRemover.isEmpty()) {
            equipeService.removerPessoasDaEquipe(equipeId, idsParaRemover);
        }

        return "redirect:/equipes/usuarios/" + equipeId;
    }

    @GetMapping("/gerarPdfTodasAsPessoasDaEquipe/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipe(@PathVariable Long id,
                                                                             @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
        List<Pessoa> representantesSelecionados = obterRepresentantesSelecionados(representanteIds);
        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);

        ByteArrayInputStream bis = pdfUtils.gerarPdfAsync(pessoasNaEquipe, equipe, subtitulo.isBlank() ? null : subtitulo)
                .join();

        return montarRespostaPdf("relatorio-equipe.pdf", bis);
    }

    @GetMapping("/gerarPdfTodasAsPessoasDaEquipeIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipeIdosos(@PathVariable Long id,
                                                                                  @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
        List<Pessoa> idososNaEquipe = pessoasNaEquipe.stream()
                .filter(p -> p.getIdade() >= 60)
                .toList();
        List<Pessoa> representantesSelecionados = obterRepresentantesSelecionados(representanteIds);
        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);

        ByteArrayInputStream bis = pdfUtils.gerarPdfAsync(idososNaEquipe, equipe, subtitulo.isBlank() ? null : subtitulo)
                .join();

        return montarRespostaPdf("idosos-equipe.pdf", bis);
    }

    @GetMapping("/gerarPdfTodasAsPessoasParticipantes/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantes(@PathVariable Long id,
                                                                                 @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> participantes = pessoaService.listarParticipantesPorEquipe(id);
        List<Pessoa> representantesSelecionados = obterRepresentantesSelecionados(representanteIds);
        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);

        ByteArrayInputStream bis = pdfUtils.gerarPdfAsync(participantes, equipe, subtitulo.isBlank() ? null : subtitulo)
                .join();

        return montarRespostaPdf("participantes-equipe.pdf", bis);
    }

    @GetMapping("/gerarPdfTodasAsPessoasParticipantesIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantesIdosos(@PathVariable Long id,
                                                                                       @RequestParam(value = "representanteIds", required = false) List<Long> representanteIds) throws IOException {
        Equipe equipe = equipeService.buscarPorId(id);
        List<Pessoa> participantes = pessoaService.listarParticipantesPorEquipe(id);
        List<Pessoa> idososParticipantes = participantes.stream()
                .filter(p -> p.getIdade() >= 60)
                .toList();
        List<Pessoa> representantesSelecionados = obterRepresentantesSelecionados(representanteIds);
        String subtitulo = PdfUtils.formatarRepresentantesSelecionados(representantesSelecionados);

        ByteArrayInputStream bis = pdfUtils.gerarPdfAsync(idososParticipantes, equipe, subtitulo.isBlank() ? null : subtitulo)
                .join();

        return montarRespostaPdf("participantes-idosos-equipe.pdf", bis);
    }

    private List<Pessoa> montarListaBase(String filtro,
                                        List<Pessoa> pessoasNaEquipe,
                                        List<Pessoa> pessoasParticipantes,
                                        Page<Pessoa> pageDisponiveis) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return new ArrayList<>(pageDisponiveis.getContent());
        }

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
        for (Pessoa pessoa : combined) {
            if (pessoa != null && pessoa.getId() != null) {
                byId.putIfAbsent(pessoa.getId(), pessoa);
            }
        }
        return new ArrayList<>(byId.values());
    }

    private List<Pessoa> obterRepresentantesSelecionados(List<Long> representanteIds) {
        return (representanteIds == null || representanteIds.isEmpty())
                ? List.of()
                : pessoaService.buscarPorIds(representanteIds);
    }

    private ResponseEntity<InputStreamResource> montarRespostaPdf(String nomeArquivo, ByteArrayInputStream bis) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
