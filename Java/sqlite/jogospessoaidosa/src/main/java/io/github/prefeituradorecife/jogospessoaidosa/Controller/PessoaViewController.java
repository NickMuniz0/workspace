package io.github.prefeituradorecife.jogospessoaidosa.Controller;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Service.DoencaService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.EquipeService;
import io.github.prefeituradorecife.jogospessoaidosa.Service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public String listar(Model model) {
        List<Pessoa> pessoas = pessoaService.listarTodas();
        model.addAttribute("pessoas", pessoas);
        return "pessoa";
    }

    @GetMapping("/cadastrar2")
    public String showSignUpForm(Model model) {
        Pessoa pessoa = new Pessoa();
        model.addAttribute("pessoa", pessoa);
        model.addAttribute("doencasDisponiveis", doencaService.listarTodas());
        model.addAttribute("equipesDisponiveis", equipeService.listarTodas());
        return "pessoaCriar2";
    }

    @GetMapping("/edit2/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Pessoa pessoa = pessoaService.buscarPorId(id);

        // Verifica se a data de nascimento está presente
        LocalDate dataNascimento = pessoa.getDataNascimento();
        if (dataNascimento != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            pessoa.setDataNascimentoFormatada(pessoa.getDataNascimento().format(formatter));
            pessoa.setDataNascimento(dataNascimento);
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            pessoa.setIdade(idade);
            pessoa.setIdoso(idade >= 60);
        }

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("doencasDisponiveis", doencaService.listarTodas());
        model.addAttribute("equipesDisponiveis", equipeService.listarTodas());

        return "pessoaEditar2";
    }

    @PostMapping("/salvarPessoa")
    public String atualizar(@ModelAttribute Pessoa pessoa) {
        if (!pessoa.getDataNascimentoFormatada().isEmpty()) {
            LocalDate dataNascimento = LocalDate.parse(pessoa.getDataNascimentoFormatada());
            pessoa.setDataNascimento(dataNascimento);
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            pessoa.setIdade(idade);
            pessoa.setIdoso(idade >= 60);
        }else{
            pessoa.setDataNascimento(null);
            pessoa.setIdade(null);
            pessoa.setIdoso(false);
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
    public String buscarPessoas(@RequestParam(required = false) String filtro, Model model) {
          model = pessoaService.buscaSpecification(filtro,model);
        return "pessoa";
    }

}
