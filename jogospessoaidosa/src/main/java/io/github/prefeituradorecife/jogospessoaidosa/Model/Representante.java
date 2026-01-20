package io.github.prefeituradorecife.jogospessoaidosa.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "representante")
public class Representante {
    @Id
    private String id;
    private String nome;
    private List<Telefone> telefones = new ArrayList<>();

}
