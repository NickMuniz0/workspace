package br.com.aulas.projeto.repositorys;

import br.com.aulas.projeto.entitys.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoEntity,Long> {

    @Query("Select aluno from AlunoEntity aluno where aluno.nome = :nome")
    List<AlunoEntity> findByNome(@Param("nome") String nome);


}
