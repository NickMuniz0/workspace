package io.github.demo1.arquiteturaspring.montadora;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chave {
    private Montadora montadora;
    private String tipo;
}
