package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Representante;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepresentanteRepository extends JpaRepository<Representante, Long> {

    @Modifying
    @Transactional
    @Query("delete from Representante r where r.pessoa.id in :pessoaIds")
    void deleteByPessoaIds(@Param("pessoaIds") List<Long> pessoaIds);
}
