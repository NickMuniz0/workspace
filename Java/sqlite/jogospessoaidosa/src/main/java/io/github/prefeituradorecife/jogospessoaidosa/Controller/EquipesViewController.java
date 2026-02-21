package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import java.util.List;

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
        model.addAttribute("equipe", new Equipe(null));
        model.addAttribute("pessoasDisponiveis", pessoaService.listarTodas());
        model.addAttribute("RPA",RPA.values());
        return "equipeCriar2";
    }

    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<Long> ids) {
        equipeService.deletarPorIds(ids);
        return "redirect:/equipes";
    }

    @GetMapping("/buscar")
    public String buscarEquipes(@RequestParam(required = false) String filtro, Model model) {
        model = equipeService.buscaSpecification(filtro,model);
        return "equipe";
    }


}
