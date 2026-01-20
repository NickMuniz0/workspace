package io.github.prefeituradorecife.jogospessoaidosa.Repository;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PessoaRepository extends MongoRepository<Pessoa, String> {
    List<Pessoa> findAll();
    List<Pessoa> findByEquipesIds(String equipeId);
    List<Pessoa> findByParticipantesJogosIds(String equipeId);

}
