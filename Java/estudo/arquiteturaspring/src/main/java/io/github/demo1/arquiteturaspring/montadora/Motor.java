package io.github.demo1.arquiteturaspring.montadora;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Motor {

    private String modelo;
    private Integer cavalos;
    private Integer cilindros;
    private Double liragem;
    private TipoMotor tipo;


    @Override
    public String toString() {
        return super.toString();
    }
}
