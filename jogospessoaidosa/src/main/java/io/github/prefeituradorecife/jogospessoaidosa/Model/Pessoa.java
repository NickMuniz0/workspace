package io.github.prefeituradorecife.jogospessoaidosa.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Document(collection = "pessoa")
public class Pessoa {
    @Id
    private String id;
    private String nome;
    private Integer idade;
    private String cpf;
    private String rg;
    private Telefones telefonesdapessoa ;

    // DATA
    private LocalDate dataNascimento ;
    private String dataNascimentoFormatada ;

    //IDOSO
    private boolean idoso;
    private String idosoTexto;

    //AVALIACAO
    private boolean avaliacaoMedica;
    private String avaliacaoMedicaTexto;

    //DOENCAS
    private List<String> doencasIds = new ArrayList<>();
    private List<Doenca> doencas = new ArrayList<>();

    //EUIPES
    private List<String> equipesIds = new ArrayList<>();
    private List<Equipe> equipes = new ArrayList<>();

    //PARTICIPANTES DOS JOGOS
    private List<String> participantesJogosIds = new ArrayList<>();
    private List<Participante> participantesJogos = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa)) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
