package io.github.prefeituradorecife.jogospessoaidosa.Controller;


import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PdfService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipes/usuarios")
public class EquipeUsuarioViewController {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/{id}")
    public String listarUsuariosDaEquipe(@PathVariable String id, Model model) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> todasAsPessoas = pessoaService.listarTodas();

        List<Pessoa> todasAsPessoasDTO = todasAsPessoas.stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        List<Pessoa> pessoasNaEquipe = todasAsPessoasDTO.stream()
                .filter(p -> p.getEquipesIds() != null && p.getEquipesIds().contains(id))
                .collect(Collectors.toList());

        List<Pessoa> pessoasDisponiveis = todasAsPessoasDTO.stream()
                .filter(p -> p.getNome() == null || !p.getId().contains(id))
                .collect(Collectors.toList());

        List<Pessoa> pessoasParticipantes =  todasAsPessoasDTO.stream()
                .filter(p -> p.getParticipantesJogosIds() != null && p.getParticipantesJogosIds().contains(id))
                .collect(Collectors.toList());

        model.addAttribute("equipe", equipe);
        model.addAttribute("pessoasNaEquipe", pessoasNaEquipe);
        model.addAttribute("pessoasDisponiveis", pessoasDisponiveis);
        model.addAttribute("pessoasParticipantes", pessoasParticipantes);
        model.addAttribute("RPA",RPA.values());

        return "equipesUsuarios";
    }

    @PostMapping("/adicionarUsuariosEquipe")
    public String adicionarUsuarios(@RequestParam String equipeId,
                                    @RequestParam(required = false) List<String> pessoaIds) {
        if (pessoaIds != null && !pessoaIds.isEmpty()) {
            pessoaService.adicionarEquipeParaPessoas(equipeId, pessoaIds);
        }else{
            // Buscar todos os IDs das pessoas disponíveis para adicionar
            List<String> todosIds = pessoaService.buscarIdsDisponiveis();
            if (!todosIds.isEmpty()) {
                pessoaService.adicionarEquipeParaPessoas(equipeId, todosIds);
            }
        }
        return "redirect:/equipes/usuarios/" + equipeId;
    }


    @PostMapping("/removerUsuariosEquipe")
    public String removerUsuarios(@RequestParam String equipeId,
                                  @RequestParam(required = false) List<String> pessoaIds) {
        if (pessoaIds != null && !pessoaIds.isEmpty()) {
            pessoaService.removerEquipeDasPessoas(equipeId, pessoaIds);
        }else{
            // Buscar todos os IDs das pessoas disponíveis para adicionar
            List<String> todosIds = pessoaService.findByEquipesIds(equipeId);
            if (!todosIds.isEmpty()) {
                pessoaService.removerEquipeDasPessoas(equipeId, todosIds);
            }
        }
        return "redirect:/equipes/usuarios/" + equipeId;
    }

    @PostMapping("/adicionarUsuariosParticipantes")
    public String adicionarUsuariosParticipantes(@RequestParam String equipeId,
                                                 @RequestParam(required = false) List<String> pessoaIds) {
        if (pessoaIds != null && !pessoaIds.isEmpty()) {
            pessoaService.adicionarParticipantes(equipeId, pessoaIds);
        }else{
            // Buscar todos os IDs das pessoas disponíveis para adicionar
            List<String> todosIds = pessoaService.findByEquipesIds(equipeId);
            if (!todosIds.isEmpty()) {
                pessoaService.adicionarParticipantes(equipeId, todosIds);
            }
        }
        return "redirect:/equipes/usuarios/" + equipeId;
    }


    @PostMapping("/removerUsuariosParticipantes")
    public String removerUsuariosParticipantes(@RequestParam String equipeId,
                                               @RequestParam(required = false) List<String> pessoaIds) {
        if (pessoaIds != null && !pessoaIds.isEmpty()) {
            pessoaService.removerParticipantes(equipeId, pessoaIds);
        }else{
            // Buscar todos os IDs das pessoas disponíveis para adicionar
            List<String> todosIds = pessoaService.findByParticipantesJogosIds(equipeId);
            if (!todosIds.isEmpty()) {
                pessoaService.removerParticipantes(equipeId, todosIds);
            }
        }
        return "redirect:/equipes/usuarios/" + equipeId;
    }

    // Métodos auxiliares para clareza
    private boolean pertenceAEquipe(Pessoa pessoa, String equipeId) {
        return pessoa.getEquipesIds() != null && pessoa.getEquipesIds().contains(equipeId);
    }

    private boolean nomeContem(Pessoa pessoa, String termoBusca) {
        return termoBusca == null || pessoa.getNome().toLowerCase().contains(termoBusca.toLowerCase());
    }

    private boolean pertenceAParticipantes(Pessoa pessoa, String equipeId) {
        return pessoa.getParticipantesJogosIds() != null && pessoa.getParticipantesJogosIds().contains(equipeId);
    }

    @GetMapping("/gerarPdfTodasAsPessoasDaEquipe/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipe( @PathVariable String id) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> todasDTOs = pessoaService.listarTodas().stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        // Filtrar pessoas que estão na equipe
        List<Pessoa> pessoasNaEquipe = todasDTOs.stream()
                .filter(p -> pertenceAEquipe(p, id))
                .collect(Collectors.toList());

        ByteArrayInputStream bis = pdfService.gerarPdf(pessoasNaEquipe,equipe);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=representantes.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/gerarPdfTodasAsPessoasDaEquipeIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasDaEquipeIdosos( @PathVariable String id) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> todasDTOs = pessoaService.listarTodas().stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        // Filtrar pessoas que estão na equipe
        List<Pessoa> pessoasNaEquipe = todasDTOs.stream()
                .filter(p -> pertenceAEquipe(p, id))
                .filter(p -> p.getIdade() >= 60)
                .collect(Collectors.toList());

        ByteArrayInputStream bis = pdfService.gerarPdf(pessoasNaEquipe,equipe);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=representantes.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/gerarPdfTodasAsPessoasParticipantes/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantes( @PathVariable String id) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> todasDTOs = pessoaService.listarTodas().stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        // Filtrar pessoas que estão na equipe
        List<Pessoa> pessoasNaEquipe = todasDTOs.stream()
                .filter(p -> pertenceAParticipantes(p, id))
//                .filter(p -> p.getIdade() >= 60)
                .collect(Collectors.toList());

        ByteArrayInputStream bis = pdfService.gerarPdf(pessoasNaEquipe,equipe);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=representantes.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping("/gerarPdfTodasAsPessoasParticipantesIdosos/{id}")
    public ResponseEntity<InputStreamResource> gerarPdfTodasAsPessoasParticipantesIdosos( @PathVariable String id) {
        Equipe equipe = equipeService.buscarPorId(id);

        List<Pessoa> todasDTOs = pessoaService.listarTodas().stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        // Filtrar pessoas que estão na equipe
        List<Pessoa> pessoasNaEquipe = todasDTOs.stream()
                .filter(p -> pertenceAParticipantes(p, id))
                .filter(p -> p.getIdade() >= 60)
                .collect(Collectors.toList());

        ByteArrayInputStream bis = pdfService.gerarPdf(pessoasNaEquipe,equipe);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=representantes.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }



}
