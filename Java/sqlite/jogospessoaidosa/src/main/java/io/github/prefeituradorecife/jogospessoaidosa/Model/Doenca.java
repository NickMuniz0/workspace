package io.github.prefeituradorecife.jogospessoaidosa.Model;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Getter
@Setter
@Entity
public class Doenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private boolean alergico;

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
