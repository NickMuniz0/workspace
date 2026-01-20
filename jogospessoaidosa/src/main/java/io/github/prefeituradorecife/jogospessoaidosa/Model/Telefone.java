package io.github.prefeituradorecife.jogospessoaidosa.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "telefone")
@AllArgsConstructor
public class Telefone {
    @Id
    private String id;
    private String dd;
    private String numero;

    public Telefone() {
    }

    public Telefone(String ddd, String numero) {
    }
}
