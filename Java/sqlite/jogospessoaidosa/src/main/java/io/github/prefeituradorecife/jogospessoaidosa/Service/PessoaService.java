package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.RepresentanteRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.PessoaSpecification;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.PessoaSpecificationBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final RepresentanteRepository representanteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PessoaService(PessoaRepository pessoaRepository, RepresentanteRepository representanteRepository) {
        this.pessoaRepository = pessoaRepository;
        this.representanteRepository = representanteRepository;
    }

    public List<Pessoa> listarTodasSemPagina() {
        return ordenarPessoas(pessoaRepository.findAll());
    }

    public Page<Pessoa> listarTodas(Pageable pageable) {
        return pessoaRepository.findAll(pageable);
    }

    public Page<Pessoa> buscarDisponiveis(String filtro, Long equipeId, Pageable pageable) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";
        if (termo.isEmpty()) {
            return pessoaRepository.findAll(pageable);
        }

        Specification<Pessoa> specTerm = PessoaSpecification.contemTermo(termo);

        List<Long> idsNaEquipe = equipeId == null ? List.of() : findByEquipesIds(equipeId);
        Specification<Pessoa> specExclusao = null;
        if (idsNaEquipe != null && !idsNaEquipe.isEmpty()) {
            specExclusao = (root, query, cb) -> cb.not(root.get("id").in(idsNaEquipe));
        }

        Specification<Pessoa> spec = specTerm;
        if (specExclusao != null) {
            spec = spec.and(specExclusao);
        }

        return pessoaRepository.findAll(spec, pageable);
    }

    public List<Pessoa> listarPorEquipe(Long equipeId) {
        return ordenarPessoas(pessoaRepository.findByEquipeIdWithDoencas(equipeId));
    }

    public List<Pessoa> listarParticipantesPorEquipe(Long equipeId) {
        return ordenarPessoas(pessoaRepository.findByParticipantesEquipeIdWithDoencas(equipeId));
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

    @Transactional
    public void salvarOuAtualizar(Pessoa pessoa) {
        if (pessoa.getTelefones() != null) {
            pessoa.getTelefones().forEach(telefone -> telefone.setPessoa(pessoa));
        }
        pessoaRepository.save(pessoa);
        entityManager.flush();
        entityManager.clear();
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
        Specification<Pessoa> spec = new PessoaSpecificationBuilder()
                .comTermo(filtro)
                .build();

        return pessoaRepository.findAll(spec, pageable);
    }

    public List<Pessoa> buscarPorFiltro(String filtro) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";

        if (termo.isEmpty()) {
            return ordenarPessoas(pessoaRepository.findAll());
        }

        return ordenarPessoas(pessoaRepository.findAll(PessoaSpecification.contemTermo(termo)));
    }

    public List<Long> buscarIdsDisponiveis() {
        return ordenarPessoas(pessoaRepository.findAll()).stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }

    private List<Pessoa> ordenarPessoas(List<Pessoa> pessoas) {
        return pessoas.stream()
                .sorted(Comparator.comparing(Pessoa::getNome, Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Pessoa::getId, Comparator.nullsLast(Long::compareTo)))
                .collect(Collectors.toList());
    }

    public List<Long> findByEquipesIds(Long equipeId) {
        return pessoaRepository.findByEquipes_Id(equipeId).stream()
                .map(Pessoa::getId)
                .collect(Collectors.toList());
    }

    public long contarTodos() {
        return pessoaRepository.count();
    }

    public List<Pessoa> buscarPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return pessoaRepository.findAllById(ids).stream()
                .sorted(Comparator.comparing(pessoa -> ids.indexOf(pessoa.getId())))
                .collect(Collectors.toList());
    }

    public void atualizarIdades() {
        pessoaRepository.atualizarIdades();
    }

    public List<Pessoa> buscarDisponiveis(String filtro, Long equipeId) {
        return pessoaRepository.buscarDisponiveis(filtro, equipeId);
    }
}
