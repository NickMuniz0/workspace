package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeRepository extends MongoRepository<Equipe, String> {
    List<Equipe> findByNomeContainingIgnoreCase(String nome);
}
