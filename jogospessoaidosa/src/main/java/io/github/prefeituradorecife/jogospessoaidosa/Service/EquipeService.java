package io.github.prefeituradorecife.jogospessoaidosa.Service;

import io.github.prefeituradorecife.jogospessoaidosa.Model.*;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.EquipeRepository;
import io.github.prefeituradorecife.jogospessoaidosa.Repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private  PessoaRepository pessoaRepository;

    public List<Equipe> listarTodas() {
        return equipeRepository.findAll();
    }

    public void salvarOuAtualizarEquipe(Equipe equipe) {
         equipeRepository.save(equipe);
    }

    public Equipe buscarPorId(String id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipe com ID " + id + " não encontrada"));
    }

    public void deletarEquipe(String id) {
        equipeRepository.deleteById(id);
    }

    public List<Equipe> buscarPorNome(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return equipeRepository.findAll();
        }
        return equipeRepository.findByNomeContainingIgnoreCase(filtro);
    }

    public void deletarPorIds(List<String> ids) {
        equipeRepository.deleteAllById(ids);
    }

}
