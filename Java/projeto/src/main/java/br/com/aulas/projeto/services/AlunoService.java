package br.com.aulas.projeto.services;

import br.com.aulas.projeto.entitys.AlunoEntity;
import br.com.aulas.projeto.repositorys.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public List<AlunoEntity> findAll(){
        return alunoRepository.findAll();
    }

    public List<AlunoEntity> findByNome(String nome){
        return alunoRepository.findByNome(nome);
        
    }

    public Long salvar(AlunoEntity aluno){
        AlunoEntity alunoSalvo = alunoRepository.save(aluno);
        return alunoSalvo != null ? alunoSalvo.getId() : null;
    }
}
