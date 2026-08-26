package io.github.prefeituradorecife.jogospessoaidosa.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Equipe;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Representante;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Telefone;

public class EquipeSpecificationBuilder {

    private final List<Specification<Equipe>> specs = new ArrayList<>();

    public EquipeSpecificationBuilder comTermo(String termo) {
        if (termo != null && !termo.isBlank()) {
            String likeTerm = "%" + termo.toLowerCase() + "%";

            specs.add((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Campos da própria equipe
                predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));
                predicates.add(cb.like(cb.lower(root.get("rpa")), likeTerm));

                // Participantes (Pessoa)
                Join<Equipe, Pessoa> participantesJoin = root.join("participantesJogos", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(participantesJoin.get("nome")), likeTerm));
                predicates.add(cb.like(cb.lower(participantesJoin.get("cpf")), likeTerm));
                predicates.add(cb.like(cb.lower(participantesJoin.get("rg")), likeTerm));

                // Representantes
                Join<Equipe, Representante> representantesJoin = root.join("representantes", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(representantesJoin.get("nome")), likeTerm));

                // Telefones dos representantes
                Join<Representante, Telefone> telefonesJoin = representantesJoin.join("telefones", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(telefonesJoin.get("dd")), likeTerm));
                predicates.add(cb.like(cb.lower(telefonesJoin.get("numero")), likeTerm));

                // Evitar duplicados
                query.distinct(true);

                return cb.or(predicates.toArray(new Predicate[0]));
            });
        }
        return this;
    }

    public Specification<Equipe> build() {
        return specs.stream().reduce(all(), Specification::and);
    }

    private Specification<Equipe> all() {
        return (root, query, cb) -> cb.conjunction();
    }
}
