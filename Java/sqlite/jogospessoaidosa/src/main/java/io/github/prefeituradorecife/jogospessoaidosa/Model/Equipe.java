package io.github.prefeituradorecife.jogospessoaidosa.Model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import jakarta.persistence.Transient;



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

    // @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Representante> representantes = new ArrayList<>();

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Representante> representantes = new HashSet<>();

    @Transient
    private List<Long> representantesIds;

    // helper para manter consistência
    public void addRepresentante(Representante representante) {
        representantes.add(representante);
        representante.setEquipe(this);
    }

    @ManyToMany
    @JoinTable(
        name = "equipe_participantes",
        joinColumns = @JoinColumn(name = "equipe_id"),
        inverseJoinColumns = @JoinColumn(name = "pessoa_id")
    )
    private List<Pessoa> participantesJogos = new ArrayList<>();

    public void addParticipante(Pessoa pessoa) {
        if (!participantesJogos.contains(pessoa)) {
            participantesJogos.add(pessoa);
        }
        if (!pessoa.getParticipantesJogos().contains(this)) {
            pessoa.getParticipantesJogos().add(this);
        }
    }

    public void removeParticipante(Pessoa pessoa) {
        participantesJogos.remove(pessoa);
        pessoa.getParticipantesJogos().remove(this);
    }



}
