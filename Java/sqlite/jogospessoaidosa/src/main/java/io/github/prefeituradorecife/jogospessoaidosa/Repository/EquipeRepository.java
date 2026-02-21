package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long>, JpaSpecificationExecutor<Equipe> {

    // Busca exata
    List<Equipe> findByNome(String nome);

    // Busca contendo parte do nome (like %nome%)
    List<Equipe> findByNomeContainingIgnoreCase(String nome);

    List<Equipe> findByRpaContainingIgnoreCase(String rpa);

}
