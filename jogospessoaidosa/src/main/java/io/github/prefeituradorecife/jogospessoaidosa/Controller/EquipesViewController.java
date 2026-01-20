package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipes")
public class EquipesViewController {
    @Autowired
    private  EquipeService equipeService;
    @Autowired
    private  PessoaService pessoaService;



    @GetMapping
    public String listar(Model model) {
        model.addAttribute("equipes", equipeService.listarTodas());
        return "equipe";
    }

    @PostMapping("/salvarEquipe")
    public String salvar(@ModelAttribute Equipe equipe) {
        equipeService.salvarOuAtualizarEquipe(equipe);
        return "redirect:/equipes";
    }

    @GetMapping("/cadastrar2")
    public String showSignUpForm2(Model model) {
        model.addAttribute("equipe", new Equipe());
        model.addAttribute("pessoasDisponiveis", pessoaService.listarTodas());
        model.addAttribute("RPA",RPA.values());
        return "equipeCriar2";
    }


    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<String> ids) {
        equipeService.deletarPorIds(ids);
        return "redirect:/equipes";
    }

    @GetMapping("/buscar")
    public String buscarEquipes(@RequestParam(required = false) String filtro, Model model) {
        List<Equipe> todas = equipeService.listarTodas();

        String termo = filtro != null ? filtro.toLowerCase() : "";
        List<Equipe> filtradas = todas.stream()
                .filter(p -> correspondeAoFiltro(p, termo))
                .collect(Collectors.toList());


        model.addAttribute("equipes", filtradas);
        model.addAttribute("filtro", filtro);

        return "equipe"; // ajuste conforme o nome do seu template
    }

    private boolean correspondeAoFiltro(Equipe p, String termo) {
        if (termo.isEmpty()) return true;

        boolean nomeMatch = p.getNome() != null && p.getNome().toLowerCase().contains(termo);
        boolean rpaMatch = p.getRpa() != null && p.getRpa().toString().toLowerCase().contains(termo);

        return nomeMatch || rpaMatch ;

    }

}
