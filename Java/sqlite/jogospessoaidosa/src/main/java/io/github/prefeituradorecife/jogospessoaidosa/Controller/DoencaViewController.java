package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Service.DoencaService;
import io.github.prefeituradorecife.jogospessoaidosa.Utils.PageSortingUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/doencas")
public class DoencaViewController {

    private static final String VIEW_LISTA = "doenca";
    private static final String VIEW_CADASTRO = "doencaCriar2";
    private static final String VIEW_EDICAO = "doencaEditar2";

    private final DoencaService doencaService;

    public DoencaViewController(DoencaService doencaService) {
        this.doencaService = doencaService;
    }

    @Cacheable("doencas")
    @GetMapping
    public String listar(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Doenca> doencasPage = PageSortingUtils.orderByName(
                doencaService.listarTodas(pageable),
                pageable,
                Doenca::getNome
        );

        model.addAttribute("doencasPage", doencasPage);
        model.addAttribute("totalDoencas", doencaService.contarTodos());
        return VIEW_LISTA;
    }

    @GetMapping("/cadastrar2")
    public String showSignUpForm2(Model model) {
        model.addAttribute("doenca", new Doenca());
        return VIEW_CADASTRO;
    }

    @PostMapping("/salvarDoenca")
    public String salvar(@ModelAttribute Doenca doenca) {
        doencaService.salvar(doenca);
        return "redirect:/doencas";
    }

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
        return VIEW_EDICAO;
    }

    @GetMapping("/buscar")
    public String buscarDoencas(@RequestParam(required = false) String filtro, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
        Page<Doenca> doencasPage = doencaService.buscaSpecification(filtro, pageable);

        model.addAttribute("doencasPage", doencasPage);
        model.addAttribute("filtro", filtro != null ? filtro : "");
        model.addAttribute("totalDoencas", doencasPage.getTotalElements());
        return VIEW_LISTA;
    }

    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<Long> ids) {
        doencaService.deletarPorIds(ids);
        return "redirect:/doencas";
    }
}
