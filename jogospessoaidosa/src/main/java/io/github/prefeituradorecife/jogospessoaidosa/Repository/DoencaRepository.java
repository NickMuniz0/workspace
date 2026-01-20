package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoencaRepository extends MongoRepository<Doenca, String> {
    List<Doenca> findByNomeContainingIgnoreCase(String nome);

}
