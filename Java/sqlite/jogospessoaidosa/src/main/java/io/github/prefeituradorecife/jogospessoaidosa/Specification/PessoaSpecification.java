package io.github.prefeituradorecife.jogospessoaidosa.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import io.github.prefeituradorecife.jogospessoaidosa.Model.Pessoa;

public class PessoaSpecification {
    public static Specification<Pessoa> contemTermo(String termo) {
        return (root, query, cb) -> {
            String likeTerm = "%" + termo.toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            // Campos String
            predicates.add(cb.like(cb.lower(root.get("nome")), likeTerm));
            predicates.add(cb.like(cb.lower(root.get("cpf")), likeTerm));
            predicates.add(cb.like(cb.lower(root.get("rg")), likeTerm));

            // Campo numérico (idade)
            try {
                int idade = Integer.parseInt(termo);
                predicates.add(cb.equal(root.get("idade"), idade));
            } catch (NumberFormatException e) {
                // ignora se não for número
            }

              // Campo booleano (idoso)
            if (termo.equalsIgnoreCase("sim") || termo.equals("1")) {
                predicates.add(cb.isTrue(root.get("idoso")));
            } else if (termo.equalsIgnoreCase("não") || termo.equals("0")) {
                predicates.add(cb.isFalse(root.get("idoso")));
            }

            // Campo booleano (avaliacaoMedica)
            if (termo.equalsIgnoreCase("sim") || termo.equals("1")) {
                predicates.add(cb.isTrue(root.get("avaliacaoMedica")));
            } else if (termo.equalsIgnoreCase("não") || termo.equals("0")) {
                predicates.add(cb.isFalse(root.get("avaliacaoMedica")));
            }



            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}