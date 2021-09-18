package br.com.site.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.site.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico,Long> {
    //Relacionamento entre tabelas procurando pelo atributo
    List<Topico> findByCurso_Nome(String nomedoCurso);
    

    //mudar o nome
    @Query("SELECT t from topico t where t.curso.nome=:nomeCurso")
    List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomedoCurso);

    
}
