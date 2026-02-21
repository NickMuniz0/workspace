package io.github.demo1.arquiteturaspring.montadora;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Carro {

    public Carro(Motor motor){
        this.motor = motor;
    }


    private String modelo;
    private Color cor;
    private Motor motor;
    private Montadora montadora;


    public CarroStatus darIngnicao(Chave chave) {
        if (chave.getMontadora()!= this.montadora){
                return new CarroStatus("Não é possível iniciar o carro com esta chave!");
        }
        return new CarroStatus("Carro ligado. Rodando com o motor: "+ motor);
    }
}
