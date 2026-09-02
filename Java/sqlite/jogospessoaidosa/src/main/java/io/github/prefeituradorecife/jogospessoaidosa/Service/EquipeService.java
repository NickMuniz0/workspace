package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.EquipeRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.EquipeSpecification;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.EquipeSpecificationBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service
public class EquipeService {
    private final EquipeRepository equipeRepository;
    private final PessoaRepository pessoaRepository;

    public EquipeService(EquipeRepository equipeRepository, PessoaRepository pessoaRepository) {
        this.equipeRepository = equipeRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Equipe> listarTodasSemPagina() {
        return equipeRepository.findAll(Sort.by("nome").ascending());
    }

    @Transactional(readOnly = true)
    public Page<Equipe> listarTodas(Pageable pageable) {
        return equipeRepository.findAll(pageable);
    }

    @Transactional
    public void salvarOuAtualizarEquipe(Equipe equipe, List<Long> representanteIds) {
        Equipe equipePersistida = (equipe.getId() != null)
                ? equipeRepository.findById(equipe.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipe não encontrada"))
                : new Equipe();

        equipePersistida.setNome(equipe.getNome());
        equipePersistida.setRpa(equipe.getRpa());
        equipePersistida.getRepresentantes().clear();

        if (representanteIds != null && !representanteIds.isEmpty()) {
            List<Pessoa> pessoas = pessoaRepository.findAllById(representanteIds);
            for (Pessoa pessoa : pessoas) {
                Representante representante = new Representante();
                representante.setPessoa(pessoa);
                representante.setNome(pessoa.getNome());
                equipePersistida.addRepresentante(representante);
            }
        }

        equipeRepository.save(equipePersistida);
    }

    public void deletarPorIds(List<Long> ids) {
        equipeRepository.deleteAllById(ids);
    }

    public void deletarEquipe(Long id) {
        equipeRepository.deleteById(id);
    }

    public List<Equipe> buscarPorFiltro(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return equipeRepository.findAll();
        }

        List<Equipe> porNome = equipeRepository.findByNomeContainingIgnoreCase(filtro);
        List<Equipe> porRpa = equipeRepository.findByRpaContainingIgnoreCase(filtro);

        return Stream.concat(porNome.stream(), porRpa.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public Equipe buscarPorId(Long id) {
        return equipeRepository.findByIdWithRepresentantes(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipe não encontrada"));
    }

    public int contarUsuariosNaEquipe(Long equipeId) {
        return pessoaRepository.findByEquipes_Id(equipeId).size();
    }

    @Transactional
    public void adicionarPessoasNaEquipe(Long equipeId, List<Long> pessoaIds) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Equipe não encontrada"));

        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa p : pessoas) {
            equipe.addParticipante(p);
        }

        equipeRepository.save(equipe);
    }

    @Transactional
    public void removerPessoasDaEquipe(Long equipeId, List<Long> pessoaIds) {
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EntityNotFoundException("Equipe não encontrada"));

        List<Pessoa> pessoas = pessoaRepository.findAllById(pessoaIds);
        for (Pessoa pessoa : pessoas) {
            equipe.removeParticipante(pessoa);
        }

        equipeRepository.save(equipe);
    }

    public Page<Equipe> buscaSpecification(String filtro, Pageable pageable) {
        Specification<Equipe> spec = new EquipeSpecificationBuilder()
                .comTermo(filtro)
                .build();

        return equipeRepository.findAll(spec, pageable);
    }

    public long contarTodos() {
        return equipeRepository.count();
    }
}
