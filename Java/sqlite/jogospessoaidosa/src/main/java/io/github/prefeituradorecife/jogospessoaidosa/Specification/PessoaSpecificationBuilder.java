package io.github.prefeituradorecife.jogospessoaidosa.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;

import jakarta.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;

public class PessoaSpecificationBuilder {

    private final List<Specification<Pessoa>> specs = new ArrayList<>();

    public PessoaSpecificationBuilder comTermo(String termo) {
        if (termo != null && !termo.isBlank()) {
            String likeTerm = "%" + termo.toLowerCase() + "%";

            specs.add((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Campos básicos
                predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));
                predicates.add(cb.like(cb.lower(root.get("cpf")), likeTerm));
                predicates.add(cb.like(cb.lower(root.get("rg")), likeTerm));

                // Idade numérica
                try {
                    int idade = Integer.parseInt(termo);
                    predicates.add(cb.equal(root.get("idade"), idade));
                } catch (NumberFormatException ignored) {}

                // Booleanos: idoso
                Predicate idosoPredicate = buildBooleanPredicate(cb, root.get("idoso"), termo, "idoso");
                if (idosoPredicate != null) predicates.add(idosoPredicate);

                // Booleanos: avaliação médica
                Predicate avaliacaoPredicate = buildBooleanPredicate(cb, root.get("avaliacaoMedica"), termo, null);
                if (avaliacaoPredicate != null) predicates.add(avaliacaoPredicate);

                // Join em doenças
                root.join("doencas", JoinType.LEFT);
                query.distinct(true);
                predicates.add(cb.like(cb.lower(root.join("doencas").get("nome")), likeTerm));

                return cb.or(predicates.toArray(new Predicate[0]));
            });
        }
        return this;
    }

    public Specification<Pessoa> build() {
        return specs.stream().reduce(all(), Specification::and);
    }

    private Specification<Pessoa> all() {
        return (root, query, cb) -> cb.conjunction();
    }

    private static Predicate buildBooleanPredicate(CriteriaBuilder cb, Expression<Boolean> field, String termo, String extra) {
        if (termo.equalsIgnoreCase("sim") || termo.equals("1") || termo.equalsIgnoreCase("true") || 
            (extra != null && termo.equalsIgnoreCase(extra))) {
            return cb.isTrue(field);
        } else if (termo.equalsIgnoreCase("nao") || termo.equalsIgnoreCase("não") || termo.equals("0") || termo.equalsIgnoreCase("false")) {
            return cb.isFalse(field);
        }
        return null;
    }
}
