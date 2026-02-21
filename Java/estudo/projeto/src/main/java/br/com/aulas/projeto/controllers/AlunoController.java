package br.com.aulas.projeto.controllers;

import br.com.aulas.projeto.entitys.AlunoEntity;
import br.com.aulas.projeto.services.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "public")
public class AlunoController {

    @Autowired
    AlunoService alunoService = new AlunoService();

    @GetMapping(value = "/alunos")
    public ResponseEntity<List<AlunoEntity>> getAlunos(){
        List<AlunoEntity> listaDeAlunos = alunoService.findAll();
        return  new ResponseEntity<>(listaDeAlunos, HttpStatus.OK);
    }

    @PostMapping(value = "/alunos")
    public ResponseEntity<Long>  postAlunos(){
        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome("Nick");
        Long id = alunoService.salvar(aluno);
        return new ResponseEntity<Long>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/alunos")
    public ResponseEntity<String>  putAlunos(){
        return new ResponseEntity<>("Retorno PUT", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/alunos")
    public String deleteAlunos(){
        return "Retorno DELETE";
    }
}
