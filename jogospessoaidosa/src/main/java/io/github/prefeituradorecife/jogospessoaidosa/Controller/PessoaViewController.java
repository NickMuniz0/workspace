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
import java.util.ArrayList;
import java.util.List;
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
    public String listar(Model model) {
        List<Pessoa> pessoas = pessoaService.listarTodas().stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        model.addAttribute("pessoas", pessoas);
        return "pessoa";
    }

    @GetMapping("/cadastrar2")
    public String showSignUpForm(Model model) {
        Pessoa pessoa = new Pessoa();
        Telefones telefones = new Telefones();
        telefones.setTelefones(new ArrayList<>());
        pessoa.setTelefonesdapessoa(telefones);
        pessoa.getTelefonesdapessoa().getTelefones().add(new Telefone());

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("doencasDisponiveis", pessoaService.listarDoencasDisponiveis());
        model.addAttribute("equipesDisponiveis", equipeService.listarTodas());
        return "pessoaCriar2";
    }

    @GetMapping("/edit2/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Pessoa pessoa = pessoaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

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
        model.addAttribute("doencasDisponiveis", pessoaService.listarDoencasDisponiveis());
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
    public String deletarMultiplos(@RequestParam List<String> idsParaExcluir) {
        pessoaService.deletarPorIds(idsParaExcluir);
        return "redirect:/pessoas";
    }

    @GetMapping("/buscar")
    public String buscarPessoas(@RequestParam(required = false) String filtro, Model model) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";

        List<Pessoa> todas = pessoaService.listarTodas().stream()
                .map(pessoaService::convert)
                .collect(Collectors.toList());

        List<Pessoa> filtradas = todas.stream()
                .filter(p -> correspondeAoFiltro(p, termo))
                .collect(Collectors.toList());

        model.addAttribute("pessoas", filtradas);
        model.addAttribute("filtro", filtro);

        return "pessoa";
    }

    private boolean correspondeAoFiltro(Pessoa p, String termo) {
        if (termo.isEmpty()) return true;

        boolean nomeMatch = p.getNome() != null && p.getNome().toLowerCase().contains(termo);
        boolean cpfMatch = p.getCpf() != null && p.getCpf().toLowerCase().contains(termo);
        boolean rgMatch = p.getRg() != null && p.getRg().toLowerCase().contains(termo);
        boolean idadeMatch = p.getIdade() != null && String.valueOf(p.getIdade()).contains(termo);
        boolean idosoMatch = termo.equals("true") && p.isIdoso()
                || termo.equals("false") && !p.isIdoso()
                || termo.equals("sim") && p.isIdoso()
                || termo.equals("não") && !p.isIdoso()
                || termo.equals("nao") && !p.isIdoso();
        boolean idosoAvaliacaoMedicaMatch = termo.equals("true") && p.isAvaliacaoMedica()
                || termo.equals("false") && !p.isAvaliacaoMedica()
                || termo.equals("sim") && p.isAvaliacaoMedica()
                || termo.equals("não") && !p.isAvaliacaoMedica()
                || termo.equals("nao") && !p.isAvaliacaoMedica();

        return nomeMatch || cpfMatch || rgMatch || idadeMatch || idosoMatch | idosoAvaliacaoMedicaMatch;

    }

}
