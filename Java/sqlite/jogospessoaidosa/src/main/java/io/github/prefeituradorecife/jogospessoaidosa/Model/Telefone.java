package io.github.prefeituradorecife.jogospessoaidosa.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dd;
    private String numero;

    @ManyToOne
    @JoinColumn(name = "representante_id")
    private Representante representante;
}