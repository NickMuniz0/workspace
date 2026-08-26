package io.github.prefeituradorecife.jogospessoaidosa.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Doenca;

import org.springframework.data.jpa.domain.Specification;

public class DoencaSpecificationBuilder {

    private final List<Specification<Doenca>> specs = new ArrayList<>();

    public DoencaSpecificationBuilder comTermo(String termo) {
        if (termo != null && !termo.isBlank()) {
            String likeTerm = "%" + termo.toLowerCase() + "%";

            specs.add((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Campos básicos
                predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));
               
                return cb.or(predicates.toArray(new Predicate[0]));
            });
        }
        return this;
    }

    public Specification<Doenca> build() {
        return specs.stream().reduce(all(), Specification::and);
    }

    private Specification<Doenca> all() {
        return (root, query, cb) -> cb.conjunction();
    }

}
