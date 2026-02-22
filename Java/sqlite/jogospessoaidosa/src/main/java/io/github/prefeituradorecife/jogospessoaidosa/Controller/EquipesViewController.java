package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Controller
@RequestMapping("/equipes")
public class EquipesViewController {
    @Autowired
    private  EquipeService equipeService;
    @Autowired
    private  PessoaService pessoaService;

    @Cacheable("equipes")
    @GetMapping
    public String listar(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Equipe> equipesPage = equipeService.listarTodas(pageable);

        model.addAttribute("equipesPage", equipesPage);
        return "equipe";
    }
    @CacheEvict(value = "equipes", allEntries = true)
    @PostMapping("/salvarEquipe")
    public String salvar(@ModelAttribute Equipe equipe) {

        equipeService.salvarOuAtualizarEquipe(equipe);
        return "redirect:/equipes";
    }
    @CacheEvict(value = "equipes", allEntries = true)
    @GetMapping("/cadastrar2")
    public String showSignUpForm2(Model model) {
        model.addAttribute("equipe", new Equipe(null));
        model.addAttribute("pessoasDisponiveis", pessoaService.listarTodasSemPagina());
        model.addAttribute("RPA",RPA.values());
        return "equipeCriar2";
    }
    @CacheEvict(value = "equipes", allEntries = true)
    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<Long> ids) {
        equipeService.deletarPorIds(ids);
        return "redirect:/equipes";
    }

    @GetMapping("/buscar")
    public String buscarEquipes(@RequestParam(required = false) String filtro, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Equipe> equipesPage = equipeService.buscaSpecification(filtro, pageable);

        model.addAttribute("equipesPage", equipesPage);
        model.addAttribute("filtro", filtro);
        return "equipe";
    }


}
