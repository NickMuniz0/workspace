package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long>, JpaSpecificationExecutor<Equipe> {

    @Override
    @EntityGraph(attributePaths = {"representantes", "representantes.pessoa"})
    Page<Equipe> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"representantes", "representantes.pessoa"})
    List<Equipe> findAll();

    @Override
    @EntityGraph(attributePaths = {"representantes", "representantes.pessoa"})
    Page<Equipe> findAll(Specification<Equipe> spec, Pageable pageable);

    // Busca exata
    List<Equipe> findByNome(String nome);

    // Busca contendo parte do nome (like %nome%)
    List<Equipe> findByNomeContainingIgnoreCase(String nome);

    List<Equipe> findByRpaContainingIgnoreCase(String rpa);

}
