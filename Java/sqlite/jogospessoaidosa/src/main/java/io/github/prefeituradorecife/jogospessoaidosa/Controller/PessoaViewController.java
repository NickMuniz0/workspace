package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Service.DoencaService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import io.github.prefeituradorecife.jogospessoaidosa.Utils.PageSortingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pessoas")
public class PessoaViewController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private DoencaService doencaService;

    @GetMapping
    public String listar(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "nome") String sortBy,
                        @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Pessoa> pessoasPage = PageSortingUtils.orderByName(
                pessoaService.listarTodas(pageable),
                pageable,
                Pessoa::getNome
        );

        model.addAttribute("pessoasPage", pessoasPage);
        model.addAttribute("totalPessoas", pessoaService.contarTodos());

        // List<Pessoa> pessoas = pessoaService.listarTodas();
        // model.addAttribute("pessoas", pessoas);
        return "pessoa";
    }
    @GetMapping("/cadastrar2")
    public String showSignUpForm(Model model) {
        Pessoa pessoa = new Pessoa();
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("doencasDisponiveis", doencaService.listarTodasSemPagina());
        model.addAttribute("equipesDisponiveis", equipeService.listarTodasSemPagina());
        return "pessoaCriar2";
    }

    @GetMapping("/edit2/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Pessoa pessoa = pessoaService.buscarPorId(id);

        // Verifica se a data de nascimento está presente
        LocalDate dataNascimento = pessoa.getDataNascimento();
        if (dataNascimento != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            pessoa.setDataNascimentoFormatada(pessoa.getDataNascimento().format(formatter));
            pessoa.setDataNascimento(dataNascimento);
            // int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            int idade = LocalDate.now().getYear() - dataNascimento.getYear();
            pessoa.setIdade(idade);
            pessoa.setIdoso(idade >= 60);
        }

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("doencasDisponiveis", doencaService.listarTodasSemPagina());
        model.addAttribute("equipesDisponiveis", equipeService.listarTodasSemPagina());

        return "pessoaEditar2";
    }
    @PostMapping("/salvarPessoa")
    public String atualizar(@ModelAttribute Pessoa pessoa, BindingResult bindingResult, Model model) {
        if (pessoa.getDataNascimentoFormatada() == null || pessoa.getDataNascimentoFormatada().isBlank()) {
            bindingResult.rejectValue("dataNascimentoFormatada", "required", "A data de nascimento é obrigatória.");
            model.addAttribute("doencasDisponiveis", doencaService.listarTodasSemPagina());
            model.addAttribute("equipesDisponiveis", equipeService.listarTodasSemPagina());
            return pessoa.getId() == null ? "pessoaCriar2" : "pessoaEditar2";
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNascimento = LocalDate.parse(pessoa.getDataNascimentoFormatada().trim(), formatter);
            pessoa.setDataNascimento(dataNascimento);
            // int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            int idade = LocalDate.now().getYear() - dataNascimento.getYear();
            pessoa.setIdade(idade);
            pessoa.setIdoso(idade >= 60);
        } catch (DateTimeParseException e) {
            bindingResult.rejectValue("dataNascimentoFormatada", "invalid", "Informe a data no formato dd/mm/aaaa.");
            model.addAttribute("doencasDisponiveis", doencaService.listarTodasSemPagina());
            model.addAttribute("equipesDisponiveis", equipeService.listarTodasSemPagina());
            return pessoa.getId() == null ? "pessoaCriar2" : "pessoaEditar2";
        }

        pessoaService.salvarOuAtualizar(pessoa);
        return "redirect:/pessoas";
    }

    @PostMapping("/deletarMultiplos")
    public String deletarMultiplos(@RequestParam List<Long> idsParaExcluir) {
        pessoaService.deletarPorIds(idsParaExcluir);
        return "redirect:/pessoas";
    }

    @GetMapping("/buscar")
    public String buscarPessoas(@RequestParam(required = false) String filtro,
                                Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
        Page<Pessoa> pessoasPage = pessoaService.buscaSpecification(filtro, pageable);

        System.out.println("[DEBUG] " + filtro + " - "
                + pessoasPage.getTotalPages() + "-"
                + pessoasPage.getSize() + "-"
                + pessoasPage.getNumber() + "-"
                + pessoasPage.getTotalElements() + "-"
                + pessoasPage.getContent().stream().findFirst().orElse(null));

        model.addAttribute("pessoasPage", pessoasPage);
        model.addAttribute("filtro", filtro != null ? filtro : "");
        model.addAttribute("totalPessoas", pessoasPage.getTotalElements());

        return "pessoa";
    }


  

    @PostMapping("/atualizarIdade")
    public String atualizarIdade() {
        pessoaService.atualizarIdades();
        return "redirect:/pessoas"; 
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Map<String,Object>> buscarPessoas(@RequestParam String filtro) {
        return pessoaService.buscarDisponiveis(filtro, null).stream()
            .map(p -> {
                Map<String,Object> map = new HashMap<>();
                map.put("id", p.getId());
                map.put("text", p.getNome());
                return map;
            })
            .toList();
    }


}
