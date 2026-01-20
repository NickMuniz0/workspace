package io.github.prefeituradorecife.jogospessoaidosa.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Document(collection = "doenca")
public class Doenca {
    @Id
    private String id;
    private String nome;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doenca)) return false;
        Doenca doenca = (Doenca) o;
        return Objects.equals(id, doenca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
