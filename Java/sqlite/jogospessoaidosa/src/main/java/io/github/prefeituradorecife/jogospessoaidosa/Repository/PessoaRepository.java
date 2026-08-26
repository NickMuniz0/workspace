package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, JpaSpecificationExecutor<Pessoa> {
    List<Pessoa> findAll();

    List<Pessoa> findByEquipes_Id(Long equipeId);

    @Query("SELECT p FROM Pessoa p JOIN p.equipes e WHERE e.id = :equipeId")
    List<Pessoa> findByEquipeId(@Param("equipeId") Long equipeId);

    // Carrega também as doenças associadas para evitar problema de lazy loading ao gerar PDF
    @Query("SELECT DISTINCT p FROM Pessoa p LEFT JOIN FETCH p.doencas d JOIN p.equipes e WHERE e.id = :equipeId")
    List<Pessoa> findByEquipeIdWithDoencas(@Param("equipeId") Long equipeId);

    @Query("SELECT DISTINCT p FROM Pessoa p LEFT JOIN FETCH p.doencas d JOIN p.participantesJogos e WHERE e.id = :equipeId")
    List<Pessoa> findByParticipantesEquipeIdWithDoencas(@Param("equipeId") Long equipeId);

    


    @Query("SELECT p FROM Pessoa p " +
        "WHERE (:filtro IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :filtro, '%'))) " +
        "AND (p.equipe IS NULL OR p.equipe.id <> :equipeId)")
    List<Pessoa> buscarDisponiveis(@Param("filtro") String filtro,
                                @Param("equipeId") Long equipeId);

    // @Transactional
    // @Modifying
    // @Query(value = "UPDATE pessoa " +
    //                "SET idade = (strftime('%Y','now') - strftime('%Y', datetime(data_nascimento / 1000, 'unixepoch'))) " +
    //                "- (strftime('%m-%d','now') < strftime('%m-%d', datetime(data_nascimento / 1000, 'unixepoch')))",
    //        nativeQuery = true)
    // void atualizarIdades();

    @Transactional
    @Modifying
    @Query(value = "UPDATE pessoa " +
                "SET idade = (strftime('%Y','now') - strftime('%Y', datetime(data_nascimento / 1000, 'unixepoch')))",
        nativeQuery = true)
    void atualizarIdades();


}
