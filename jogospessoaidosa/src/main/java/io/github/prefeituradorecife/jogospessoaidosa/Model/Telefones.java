package io.github.prefeituradorecife.jogospessoaidosa.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Setter
@Document(collection = "telefones")
public class Telefones {
    @Id
    private String id;
    private ArrayList<Telefone> telefones= new ArrayList<Telefone>();
}
