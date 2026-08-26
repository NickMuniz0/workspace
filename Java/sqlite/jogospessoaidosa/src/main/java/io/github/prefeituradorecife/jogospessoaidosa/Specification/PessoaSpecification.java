package io.github.prefeituradorecife.jogospessoaidosa.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import jakarta.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;
import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;

public class PessoaSpecification {


    public static Specification<Pessoa> contemTermo(String termo) {
        return (root, query, cb) -> {
            String likeTerm = "%" + termo.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));
            predicates.add(cb.like(cb.lower(root.get("cpf")), likeTerm));
            predicates.add(cb.like(cb.lower(root.get("rg")), likeTerm));

            try {
                int idade = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("idade"), idade));
            } catch (NumberFormatException ignored) {}

            // Boolean fields
            Predicate idosoPredicate = buildBooleanPredicate(cb, root.get("idoso"), termo);
            if (idosoPredicate != null) predicates.add(idosoPredicate);

            Predicate avaliacaoPredicate = buildBooleanPredicate(cb, root.get("avaliacaoMedica"), termo);
            if (avaliacaoPredicate != null) predicates.add(avaliacaoPredicate);

            // Join em doencas
            if (query != null) {
                root.join("doencas", JoinType.LEFT);
                query.distinct(true); // evita duplicados
                predicates.add(cb.like(cb.lower(root.join("doencas").get("nome")), likeTerm));
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildBooleanPredicate(CriteriaBuilder cb, Expression<Boolean> field, String termo) {
        if (termo.equalsIgnoreCase("sim") || termo.equals("1") || termo.equalsIgnoreCase("true") || termo.equalsIgnoreCase("idoso")) {
            return cb.isTrue(field);
        } else if (termo.equalsIgnoreCase("nao") || termo.equalsIgnoreCase("não") || termo.equals("0") || termo.equalsIgnoreCase("false")) {
            return cb.isFalse(field);
        }
        return null;
    }
   
   
    public static Specification<Pessoa> notInEquipe(Long equipeId) {
        return (root, query, cb) -> {
            if (equipeId == null) return cb.conjunction();

            // Subquery para selecionar pessoas que pertencem à equipe
            Subquery<Long> sub = query.subquery(Long.class);
            Root<Pessoa> subRoot = sub.from(Pessoa.class);
            Join<Object, Object> join = subRoot.join("equipes");
            sub.select(subRoot.get("id")).where(cb.equal(join.get("id"), equipeId));

            return cb.not(root.get("id").in(sub));
        };
    }

}