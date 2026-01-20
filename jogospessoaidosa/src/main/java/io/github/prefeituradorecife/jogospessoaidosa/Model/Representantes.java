package io.github.prefeituradorecife.jogospessoaidosa.Model;

import io.micrometer.observation.ObservationFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "representantes")
public class Representantes {
    @Id
    private String id;
    private List<Representante> representantes = new ArrayList<>();

}

