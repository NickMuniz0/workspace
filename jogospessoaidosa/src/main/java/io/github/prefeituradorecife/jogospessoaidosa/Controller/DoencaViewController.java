package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Service.DoencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doencas")
public class DoencaViewController {

    @Autowired
    private DoencaService doencaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("doencas", doencaService.listarTodas());
        return "doenca";
    }

    @GetMapping("/cadastrar2")
    public String showSignUpForm2(Model model) {
        model.addAttribute("doenca", new Doenca());
        return "doencaCriar2";
    }

    @PostMapping("/salvarDoenca")
    public String salvar(@ModelAttribute Doenca doenca) {
        doencaService.salvar(doenca);
        return "redirect:/doencas";
    }

    @PostMapping("/deletar")
    public String deletar(@RequestParam String id) {
        doencaService.deletarPorId(id);
        return "redirect:/doencas";
    }

    @GetMapping("/edit2/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        Doenca doenca = doencaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doença não encontrada"));
        model.addAttribute("doenca", doenca);
        return "doencaEditar2";
    }

    @GetMapping("/buscar")
    public String buscarDoencas(@RequestParam(required = false) String filtro, Model model) {
        List<Doenca> todas = doencaService.listarTodas();

        String termo = filtro != null ? filtro.toLowerCase() : "";

        List<Doenca> filtradas = todas.stream()
                .filter(d -> termo.isEmpty() || d.getNome().toLowerCase().contains(termo))
                .collect(Collectors.toList());

        model.addAttribute("doencas", filtradas);
        model.addAttribute("filtro", filtro);

        return "doenca"; // ajuste conforme o nome do seu template
    }

    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam("idsParaExcluir") List<String> ids) {
        doencaService.deletarPorIds(ids);
        return "redirect:/doencas";
    }

}
