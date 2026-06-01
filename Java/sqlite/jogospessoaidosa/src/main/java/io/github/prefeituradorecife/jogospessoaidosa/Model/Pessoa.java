package io.github.prefeituradorecife.jogospessoaidosa.Model;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Getter
@Setter
@Entity
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer idade;
    private String cpf;
    private String rg;

    private LocalDate dataNascimento;
    private String dataNascimentoFormatada;

    private boolean idoso;


    @ManyToMany
    @JoinTable(
        name = "pessoa_doencas",
        joinColumns = @JoinColumn(name = "pessoa_id"),
        inverseJoinColumns = @JoinColumn(name = "doenca_id")
    )
    private List<Doenca> doencas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "pessoa_equipes",
        joinColumns = @JoinColumn(name = "pessoa_id"),
        inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    private List<Equipe> equipes = new ArrayList<>();

    @ManyToMany(mappedBy = "participantesJogos")
    private List<Equipe> participantesJogos = new ArrayList<>();


   
}