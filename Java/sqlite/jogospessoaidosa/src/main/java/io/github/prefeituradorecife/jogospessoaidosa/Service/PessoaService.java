package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.RepresentanteRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.PessoaSpecification;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private RepresentanteRepository representanteRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> listarTodasSemPagina() {
        return pessoaRepository.findAll();
    }

    public Page<Pessoa> listarTodas(Pageable pageable) {
        return pessoaRepository.findAll(pageable);
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
        if (pessoa.getTelefones() != null) {
            pessoa.getTelefones().forEach(telefone -> telefone.setPessoa(pessoa));
        }
        pessoaRepository.save(pessoa);
    }

    @Transactional
    public void deletarPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        representanteRepository.deleteByPessoaIds(ids);
        pessoaRepository.deleteAllByIdInBatch(ids);
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada: " + id));
    }

    public Page<Pessoa> buscaSpecification(String filtro, Pageable pageable) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";

        if (termo.isEmpty()) {
            return pessoaRepository.findAll(pageable);
        }

        return pessoaRepository.findAll(PessoaSpecification.contemTermo(termo), pageable);
    }

    public List<Pessoa> buscarPorFiltro(String filtro) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";

        if (termo.isEmpty()) {
            return pessoaRepository.findAll();
        }

        return pessoaRepository.findAll(PessoaSpecification.contemTermo(termo));
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
