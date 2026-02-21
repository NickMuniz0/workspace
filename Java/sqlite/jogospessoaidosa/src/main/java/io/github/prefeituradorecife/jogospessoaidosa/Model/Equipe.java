package io.github.prefeituradorecife.jogospessoaidosa.Model;
import java.util.ArrayList;
import java.util.List;

import io.github.prefeituradorecife.jogospessoaidosa.Enum.RPA;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;



@Getter
@Setter
@Entity
@NoArgsConstructor // importante para JPA
public class Equipe {

    public Equipe(Long equipeId) {
        this.id = equipeId; // agora atribui corretamente
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private RPA rpa;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Representante> representantes = new ArrayList<>();

    // helper para manter consistência
    public void addRepresentante(Representante representante) {
        representantes.add(representante);
        representante.setEquipe(this);
    }

    @ManyToMany
    @JoinTable(
        name = "equipe_pessoa",
        joinColumns = @JoinColumn(name = "equipe_id"),
        inverseJoinColumns = @JoinColumn(name = "pessoa_id")
    )
    private List<Pessoa> participantesJogos = new ArrayList<>();


    public void addParticipante(Pessoa pessoa) {
        participantesJogos.add(pessoa);
    }

    // Método utilitário para remover participante
    public void removeParticipante(Pessoa pessoa) {
        this.participantesJogos.remove(pessoa);
    }



}
