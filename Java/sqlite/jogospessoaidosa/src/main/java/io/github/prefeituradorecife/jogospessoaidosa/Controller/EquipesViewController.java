package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import io.github.prefeituradorecife.jogospessoaidosa.Utils.PageSortingUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;


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
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "nome") String sortBy,
                        @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Equipe> equipesPage = PageSortingUtils.orderByName(
                equipeService.listarTodas(pageable),
                pageable,
                Equipe::getNome
        );

        Map<Long, Integer> quantidadeUsuariosNaEquipeMap = new LinkedHashMap<>();
        for (Equipe equipe : equipesPage.getContent()) {
            quantidadeUsuariosNaEquipeMap.put(equipe.getId(), equipeService.contarUsuariosNaEquipe(equipe.getId()));
        }

        model.addAttribute("equipesPage", equipesPage);
        model.addAttribute("totalEquipes", equipeService.contarTodos());
        model.addAttribute("quantidadeUsuariosNaEquipeMap", quantidadeUsuariosNaEquipeMap);
        return "equipe";
    }
    @PostMapping("/salvarEquipe")
    public String salvar(@ModelAttribute Equipe equipe,
                         @RequestParam(required = false) List<Long> representantes) {

        equipeService.salvarOuAtualizarEquipe(equipe, equipe.getRepresentantesIds());
        return "redirect:/equipes";
    }
    @GetMapping("/cadastrar2")
    public String showSignUpForm2(Model model,
                                  @RequestParam(required = false) String filtro,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  HttpServletRequest request) {
        model.addAttribute("equipe", new Equipe(null));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
        var pagePessoas = pessoaService.buscarDisponiveis(filtro, null, pageable);

        model.addAttribute("pessoasDisponiveis", pagePessoas.getContent());
        model.addAttribute("currentPage", pagePessoas.getNumber());
        model.addAttribute("totalPages", pagePessoas.getTotalPages());
        model.addAttribute("totalItems", pagePessoas.getTotalElements());
        model.addAttribute("pageSize", pagePessoas.getSize());

        model.addAttribute("filtro", filtro);
        model.addAttribute("RPA",RPA.values());

        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "equipeCriar2 :: representantesFragment";
        }

        return "equipeCriar2";
    }
    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<Long> ids) {
        equipeService.deletarPorIds(ids);
        return "redirect:/equipes";
    }

    @GetMapping("/buscar")
    public String buscarEquipes(@RequestParam(required = false) String filtro, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
        Page<Equipe> equipesPage = equipeService.buscaSpecification(filtro, pageable);
  
        Map<Long, Integer> quantidadeUsuariosNaEquipeMap = new LinkedHashMap<>();
        for (Equipe equipe : equipesPage.getContent()) {
            quantidadeUsuariosNaEquipeMap.put(equipe.getId(), equipeService.contarUsuariosNaEquipe(equipe.getId()));
        }

        System.out.println("[DEBUG] " + filtro + " - "
        + equipesPage.getTotalPages() + "-"
        + equipesPage.getSize() + "-"
        + equipesPage.getNumber() + "-"
        + equipesPage.getTotalElements() + "-"
        + equipesPage.getContent().stream().findFirst().orElse(null));

        model.addAttribute("equipesPage", equipesPage);
        model.addAttribute("filtro", filtro);
        model.addAttribute("totalEquipes", equipesPage.getTotalElements());
        model.addAttribute("quantidadeUsuariosNaEquipeMap", quantidadeUsuariosNaEquipeMap);
        return "equipe";
    }


}
