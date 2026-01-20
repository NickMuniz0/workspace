package io.github.prefeituradorecife.jogospessoaidosa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Document(collection = "equipe")
public class Equipe {
    @Id
    private String id;
    private String nome;
    private Telefones telefonesdapessoa;
    private List<Representante> representantes;
    private RPA rpa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipe)) return false;
        Equipe equipe = (Equipe) o;
        return Objects.equals(id, equipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}
