package io.github.prefeituradorecife.jogospessoaidosa.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "participante")
public class Participante {
    @Id
    private String id;
}
