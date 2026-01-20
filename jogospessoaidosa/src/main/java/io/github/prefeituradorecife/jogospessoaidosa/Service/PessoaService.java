package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.DoencaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.EquipeRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private DoencaRepository doencaRepository;
    @Autowired
    private EquipeRepository equipeRepository;

    public Pessoa convert(Pessoa pessoa) {

        LocalDate dataNascimento = pessoa.getDataNascimento(); // já é LocalDate
        if (dataNascimento != null) {
            pessoa.setDataNascimento(dataNascimento);
            int idade = Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
            pessoa.setIdade(idade);
            pessoa.setIdoso(idade >= 60);
        } else {
            pessoa.setDataNascimento(LocalDate.now());
            pessoa.setIdade(pessoa.getIdade());
            pessoa.setIdoso(pessoa.isIdoso());
        }
        pessoa.setIdosoTexto(pessoa.isIdoso()? "Sim" : "Não");
        pessoa.setAvaliacaoMedicaTexto(pessoa.isAvaliacaoMedica()? "Sim" : "Não");

        pessoa.setDoencasIds(pessoa.getDoencasIds());
        pessoa.setEquipesIds(pessoa.getEquipesIds());

        // Carrega os objetos completos
        if (pessoa.getDoencasIds() != null) {
            pessoa.setDoencas(doencaRepository.findAllById(pessoa.getDoencasIds()));
        }
        if (pessoa.getEquipesIds() != null) {
            pessoa.setEquipes(equipeRepository.findAllById(pessoa.getEquipesIds()));
        }

        return pessoa;
    }

    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    public List<Doenca> listarDoencasDisponiveis() {
        return doencaRepository.findAll();
    }

    public Optional<Pessoa> buscarPorId(String id) {
        return pessoaRepository.findById(id);
    }

    public void salvarOuAtualizar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    public void adicionarEquipeParaPessoas(String equipeId, List<String> pessoaIds) {
        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getEquipesIds() == null) {
                pessoa.setEquipesIds(new ArrayList<>());
            }
            if (!pessoa.getEquipesIds().contains(equipeId)) {
                pessoa.getEquipesIds().add(equipeId);
                pessoaRepository.save(pessoa);
            }
        }
    }
    public void adicionarParticipantes(String equipeId, List<String> pessoaIds) {
        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getParticipantesJogosIds() == null) {
                pessoa.setParticipantesJogosIds(new ArrayList<>());
            }
            if (!pessoa.getParticipantesJogosIds().contains(equipeId)) {
                pessoa.getParticipantesJogosIds().add(equipeId);
                pessoaRepository.save(pessoa);
            }
        }
    }
    public void removerEquipeDasPessoas(String equipeId, List<String> pessoaIds) {
        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getEquipesIds() != null && pessoa.getEquipesIds().contains(equipeId)) {
                pessoa.getEquipesIds().remove(equipeId);
                pessoaRepository.save(pessoa);
            }
        }
    }

    public void removerParticipantes(String equipeId, List<String> pessoaIds) {
        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getParticipantesJogosIds() != null && pessoa.getParticipantesJogosIds().contains(equipeId)) {
                pessoa.getParticipantesJogosIds().remove(equipeId);
                pessoaRepository.save(pessoa);
            }
        }
    }

    public void deletarPorIds(List<String> ids) {
        pessoaRepository.deleteAllById(ids);
    }

    public List<String> buscarIdsDisponiveis() {
        return pessoaRepository.findAll().stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }

    public List<String> findByEquipesIds(String equipeId) {
        return pessoaRepository.findByEquipesIds(equipeId).stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }

    public List<String> findByParticipantesJogosIds(String equipeId) {
        return pessoaRepository.findByParticipantesJogosIds(equipeId).stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }




}
