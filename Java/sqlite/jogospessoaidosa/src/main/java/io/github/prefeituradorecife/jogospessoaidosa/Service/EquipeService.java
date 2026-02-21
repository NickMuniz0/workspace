package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.EquipeRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Specification.EquipeSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EquipeService {
    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Equipe> listarTodas() {
        return equipeRepository.findAll();
    }

    public void salvarOuAtualizarEquipe(Equipe equipe) {
              
        for (Representante rep : equipe.getRepresentantes()) {
            rep.setEquipe(equipe);
            for (Telefone tel : rep.getTelefones()) {
                tel.setRepresentante(rep);
            }
        }
        equipeRepository.save(equipe);
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
        return equipeRepository.getReferenceById(id);
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

    public Model buscaSpecification(String filtro,Model model){
        String termo = filtro != null ? filtro.trim().toLowerCase() : "";
        List<Equipe> filtradas = termo.isEmpty()
                ? equipeRepository.findAll()
                : equipeRepository.findAll(EquipeSpecification.contemTermo(termo));

        model.addAttribute("equipes", filtradas);
        return model;
    }

 
}
