package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, JpaSpecificationExecutor<Pessoa> {
    List<Pessoa> findAll();

    List<Pessoa> findByEquipes_Id(Long equipeId);

    @Query("SELECT p FROM Pessoa p JOIN p.participantesJogos e WHERE e.id = :equipeId")
    List<Pessoa> findByEquipeId(@Param("equipeId") Long equipeId);


}
