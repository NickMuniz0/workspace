package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.PessoaSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    public List<Pessoa> listarPorEquipe(Long equipeId) {
        return pessoaRepository.findByEquipes_Id(equipeId);
    }

    public List<Pessoa> listarParticipantesPorEquipe(Long equipeId) {
        return pessoaRepository.findByEquipeId(equipeId);
    }

    public void adicionarEquipeParaPessoas(Long equipeId, List<Long> pessoaIds) {
        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa p : pessoas) {
            p.getEquipes().add(new Equipe(equipeId));
        }
        pessoaRepository.saveAll(pessoas);
    }

    public void removerEquipeDasPessoas(Long equipeId, List<Long> pessoaIds) {
        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa p : pessoas) {
            p.getEquipes().removeIf(e -> e.getId().equals(equipeId));
        }
        pessoaRepository.saveAll(pessoas);
    }

    public void salvarOuAtualizar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    public void deletarPorIds(List<Long> ids) {
        pessoaRepository.deleteAllByIdInBatch(ids);
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.getReferenceById(id);
    }

    public Model buscaSpecification(String filtro,Model model){
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";
        List<Pessoa> filtradas = termo.isEmpty()
                ? pessoaRepository.findAll()
                : pessoaRepository.findAll(PessoaSpecification.contemTermo(termo));

        model.addAttribute("pessoas", filtradas);
        return model;
    }

    public List<Long> buscarIdsDisponiveis() {
        return pessoaRepository.findAll().stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }

    public List<Long> findByEquipesIds(Long equipeId) {
        return pessoaRepository.findByEquipes_Id(equipeId).stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }

}
