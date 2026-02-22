package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Service.DoencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Controller
@RequestMapping("/doencas")
public class DoencaViewController {

    @Autowired
    private DoencaService doencaService;

    @Cacheable("doencas")
    @GetMapping
    public String listar(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size

    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Doenca> doencasPage = doencaService.listarTodas(pageable);

        model.addAttribute("doencasPage", doencasPage);
        // model.addAttribute("doencas", doencaService.listarTodas());
        return "doenca";
    }
    @CacheEvict(value = "doencas", allEntries = true)
    @GetMapping("/cadastrar2")
    public String showSignUpForm2(Model model) {
        model.addAttribute("doenca", new Doenca());
        return "doencaCriar2";
    }
    @CacheEvict(value = "doencas", allEntries = true)
    @PostMapping("/salvarDoenca")
    public String salvar(@ModelAttribute Doenca doenca) {
        doencaService.salvar(doenca);
        return "redirect:/doencas";
    }
    @CacheEvict(value = "doencas", allEntries = true)
    @PostMapping("/deletar")
    public String deletar(@RequestParam Long id) {
        doencaService.deletarPorId(id);
        return "redirect:/doencas";
    }

    @GetMapping("/edit2/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Doenca doenca = doencaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doença não encontrada"));
        model.addAttribute("doenca", doenca);
        return "doencaEditar2";
    }

    @GetMapping("/buscar")
    public String buscarDoencas(@RequestParam(required = false) String filtro, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Doenca> pessoasPage = doencaService.buscaSpecification(filtro, pageable);

        model.addAttribute("doencasPage", pessoasPage);
        model.addAttribute("filtro", filtro);
        return "doenca"; // ajuste conforme o nome do seu template
    }

    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<Long> ids) {
        doencaService.deletarPorIds(ids);
        return "redirect:/doencas";
    }

}
