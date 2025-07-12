package io.github.cursonick.produtosapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_produto")
public class Produto {


    @Id
    @Column(name="id")
    @Getter
    @Setter
    private String id_produto;

    @Getter
    @Setter
    private String nome;

    @Getter
    @Setter
    private String descricao;

    @Getter
    @Setter
    private Double preco;

    @Override
    public String toString() {
        return getId_produto();
    }
}
