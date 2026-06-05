package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.EquipeRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.EquipeSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EquipeService {
    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Equipe> listarTodasSemPagina() {
        return equipeRepository.findAll();
    }

    public Page<Equipe> listarTodas(Pageable pageable) {
        return equipeRepository.findAll(pageable);
    }

    @Transactional
    public void salvarOuAtualizarEquipe(Equipe equipe, List<Long> representanteIds) {
        Equipe equipePersistida = (equipe.getId() != null)
                ? equipeRepository.findById(equipe.getId()).orElseGet(() -> equipeRepository.save(equipe))
                : equipeRepository.save(equipe);

        equipePersistida.setNome(equipe.getNome());
        equipePersistida.setRpa(equipe.getRpa());

        equipePersistida.getRepresentantes().clear();

        if (representanteIds != null && !representanteIds.isEmpty()) {
            List<Pessoa> pessoas = pessoaRepository.findAllById(representanteIds);
            for (Pessoa pessoa : pessoas) {
                Representante representante = new Representante();
                representante.setPessoa(pessoa);
                representante.setNome(pessoa.getNome());
                representante.setEquipe(equipePersistida);

                equipePersistida.getRepresentantes().add(representante);
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
        // busca por nome OU rpa
        List<Equipe> porNome = equipeRepository.findByNomeContainingIgnoreCase(filtro);
        List<Equipe> porRpa = equipeRepository.findByRpaContainingIgnoreCase(filtro);

        return Stream.concat(porNome.stream(), porRpa.stream())
                    .distinct()
                    .collect(Collectors.toList());
    }

    public Equipe buscarPorId(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipe não encontrada"));
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
        equipe.getParticipantesJogos().removeAll(pessoas);

        equipeRepository.save(equipe);
    }

    public Page<Equipe> buscaSpecification(String filtro, Pageable pageable) {
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";

        if (termo.isEmpty()) {
            // retorna todos paginados
            return equipeRepository.findAll(pageable);
        } else {
            // aplica Specification com paginação
            return equipeRepository.findAll(EquipeSpecification.contemTermo(termo), pageable);
        }
    }
 
}
