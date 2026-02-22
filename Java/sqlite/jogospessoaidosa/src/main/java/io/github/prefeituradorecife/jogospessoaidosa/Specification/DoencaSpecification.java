package io.github.prefeituradorecife.jogospessoaidosa.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;


public class DoencaSpecification {
    public static Specification<Doenca> contemTermo(String termo) {
        return (root, query, cb) -> {
            String likeTerm = "%" + termo.toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            // Campos String
            predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}