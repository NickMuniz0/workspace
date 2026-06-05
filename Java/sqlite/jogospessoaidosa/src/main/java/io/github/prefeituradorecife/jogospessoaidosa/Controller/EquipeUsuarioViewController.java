package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Utils.PdfUtils;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/equipes/usuarios")
public class EquipeUsuarioViewController {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PdfUtils pdfUtils;

    @Cacheable("equipesView")
    @GetMapping("/{id}")
    public String listarUsuariosDaEquipe(@PathVariable Long id, Model model) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
        List<Pessoa> pessoasParticipantes = pessoaService.listarParticipantesPorEquipe(id);

        // Disponíveis = todas menos as que já estão na equipe
        List<Pessoa> todas = pessoaService.listarTodasSemPagina();
        List<Pessoa> pessoasDisponiveis = todas.stream()
                .filter(p -> !p.getEquipes().contains(equipe))
                .toList();

        List<Pessoa> pessoasBase = pessoaService.listarTodasSemPagina();

        model.addAttribute("equipe", equipe);
        model.addAttribute("pessoasNaEquipe", pessoasNaEquipe);
        model.addAttribute("pessoasDisponiveis", pessoasDisponiveis);
        model.addAttribute("pessoasBase", pessoasBase);
        model.addAttribute("representantesSelecionados", equipe.getRepresentantes().stream()
                .map(representante -> representante.getPessoa() != null ? representante.getPessoa().getId() : null)
                .filter(java.util.Objects::nonNull)
                .toList());
        model.addAttribute("pessoasParticipantes", pessoasParticipantes);
        model.addAttribute("RPA", RPA.values());

        return "equipesUsuarios";
    }

    @GetMapping("/{id}/buscar")
    public String buscarUsuariosDaEquipe(@PathVariable Long id,
                                         @RequestParam(required = false) String filtro,
                                         Model model) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);
        List<Pessoa> pessoasParticipantes = pessoaService.listarParticipantesPorEquipe(id);
        List<Pessoa> pessoasFiltradas = pessoaService.buscarPorFiltro(filtro);

        List<Pessoa> pessoasDisponiveis = pessoasFiltradas.stream()
                .filter(p -> !p.getEquipes().contains(equipe))
                .toList();

        model.addAttribute("equipe", equipe);
        model.addAttribute("pessoasNaEquipe", pessoasNaEquipe);
        model.addAttribute("pessoasDisponiveis", pessoasDisponiveis);
        model.addAttribute("pessoasParticipantes", pessoasParticipantes);
        model.addAttribute("pessoasBase", pessoasFiltradas);
        model.addAttribute("representantesSelecionados", equipe.getRepresentantes().stream()
                .map(representante -> representante.getPessoa() != null ? representante.getPessoa().getId() : null)
                .filter(java.util.Objects::nonNull)
                .toList());
        model.addAttribute("filtro", filtro);
        model.addAttribute("RPA", RPA.values());

        return "equipesUsuarios";
    }
    @CacheEvict(value = {"equipes", "equipesView"}, allEntries = true)
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

    @CacheEvict(value = {"equipes", "equipesView"}, allEntries = true)
    @PostMapping("/removerUsuariosEquipe")
    public String removerUsuarios(@RequestParam Long equipeId,
                                  @RequestParam(required = false) List<Long> pessoaIds) {
        if (pessoaIds != null && !pessoaIds.isEmpty()) {
            pessoaService.removerEquipeDasPessoas(equipeId, pessoaIds);
        }else{
            // Buscar todos os IDs das pessoas disponíveis para adicionar
            List<Long> todosIds = pessoaService.findByEquipesIds(equipeId);
            if (!todosIds.isEmpty()) {
                pessoaService.removerEquipeDasPessoas(equipeId, todosIds);
            }
        }
        return "redirect:/equipes/usuarios/" + equipeId;
    }
    @CacheEvict(value = {"equipes", "equipesView"}, allEntries = true)
    @PostMapping("/adicionarUsuariosParticipantes")
    public String adicionarUsuariosParticipantes(@RequestParam Long equipeId,
                                                 @RequestParam(required = false) List<Long> pessoaIds) {
        if (pessoaIds == null || pessoaIds.isEmpty()) {
            pessoaIds = pessoaService.listarTodasSemPagina()
                    .stream()
                    .map(Pessoa::getId)
                    .toList();
        }

        equipeService.adicionarPessoasNaEquipe(equipeId, pessoaIds);

        return "redirect:/equipes/usuarios/" + equipeId;
    }

    @CacheEvict(value = {"equipes", "equipesView"}, allEntries = true)
    @PostMapping("/removerUsuariosParticipantes")
    public String removerUsuariosParticipantes(@RequestParam Long equipeId,
                                            @RequestParam(required = false) List<Long> pessoaIds) {

        // Se não vier nada, remove todos os participantes da equipe
        List<Long> idsParaRemover = (pessoaIds == null || pessoaIds.isEmpty())
                ? pessoaService.listarPorEquipe(equipeId).stream().map(Pessoa::getId).toList()
                : pessoaIds;

        equipeService.removerPessoasDaEquipe(equipeId, idsParaRemover);

        return "redirect:/equipes/usuarios/" + equipeId;
    }

// ###############################################################################################################################
    @GetMapping("/gerarPdfTodasAsPessoasDaEquipe/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipe(@PathVariable Long id) throws IOException {
        // Buscar equipe pelo ID
        Equipe equipe = equipeService.buscarPorId(id);

        // Buscar todas as pessoas vinculadas à equipe
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);

        // Gerar PDF com PDFBox
        ByteArrayInputStream bis = pdfUtils.gerarPdf(pessoasNaEquipe, equipe);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    @GetMapping("/gerarPdfTodasAsPessoasDaEquipeIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipeIdosos(@PathVariable Long id) throws IOException {
        // Buscar equipe pelo ID
        Equipe equipe = equipeService.buscarPorId(id);

        // Buscar todas as pessoas da equipe
        List<Pessoa> pessoasNaEquipe = pessoaService.listarPorEquipe(id);

        // Filtrar apenas idosos (idade >= 60)
        List<Pessoa> idososNaEquipe = pessoasNaEquipe.stream()
                .filter(p -> p.getIdade() >= 60)
                .toList();

        // Gerar PDF com PDFBox
        ByteArrayInputStream bis = pdfUtils.gerarPdf(idososNaEquipe, equipe);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=idosos-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/gerarPdfTodasAsPessoasParticipantes/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantes(@PathVariable Long id) throws IOException {
        // Buscar equipe pelo ID
        Equipe equipe = equipeService.buscarPorId(id);

        // Buscar todas as pessoas participantes da equipe
        List<Pessoa> participantes = pessoaService.listarParticipantesPorEquipe(id);

        // Gerar PDF com PDFBox
        ByteArrayInputStream bis = pdfUtils.gerarPdf(participantes, equipe);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=participantes-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    @GetMapping("/gerarPdfTodasAsPessoasParticipantesIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantesIdosos(@PathVariable Long id) throws IOException {
        // Buscar equipe pelo ID
        Equipe equipe = equipeService.buscarPorId(id);

        // Buscar todos os participantes da equipe
        List<Pessoa> participantes = pessoaService.listarParticipantesPorEquipe(id);

        // Filtrar apenas idosos (idade >= 60)
        List<Pessoa> idososParticipantes = participantes.stream()
                .filter(p -> p.getIdade() >= 60)
                .toList();

        // Gerar PDF com PDFBox
        ByteArrayInputStream bis = pdfUtils.gerarPdf(idososParticipantes, equipe);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=participantes-idosos-equipe.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


}
