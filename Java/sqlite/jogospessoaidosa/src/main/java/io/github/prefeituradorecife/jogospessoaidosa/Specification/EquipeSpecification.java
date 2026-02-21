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

public class EquipeSpecification {
    public static Specification<Equipe> contemTermo(String termo) {
        return (root, query, cb) -> {
            String likeTerm = "%" + termo.toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            // Campos String
            predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));
            predicates.add(cb.like(cb.lower(root.get("rpa")), likeTerm));

            // Join com participantes (Pessoa)
            Join<Equipe, Pessoa> participantesJoin = root.join("participantesJogos", JoinType.LEFT);

            // Campos da pessoa (participante)
            predicates.add(cb.like(cb.lower(participantesJoin.get("nome")), likeTerm));
            predicates.add(cb.like(cb.lower(participantesJoin.get("cpf")), likeTerm));
            predicates.add(cb.like(cb.lower(participantesJoin.get("rg")), likeTerm));


            // Join com representantes
            Join<Equipe, Representante> representantesJoin = root.join("representantes", JoinType.LEFT);
            predicates.add(cb.like(cb.lower(representantesJoin.get("nome")), likeTerm));

            // Join com telefones do representante
            Join<Representante, Telefone> telefonesJoin = representantesJoin.join("telefones", JoinType.LEFT);
            predicates.add(cb.like(cb.lower(telefonesJoin.get("dd")), likeTerm));
            predicates.add(cb.like(cb.lower(telefonesJoin.get("numero")), likeTerm));



            // Evitar duplicados quando há várias pessoas
            query.distinct(true);

     
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}