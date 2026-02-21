package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface DoencaRepository extends JpaRepository<Doenca, Long> {

    // Busca exata
    List<Doenca> findByNome(String nome);

    // Busca contendo parte do nome (like %nome%)
    List<Doenca> findByNomeContainingIgnoreCase(String nome);


}
