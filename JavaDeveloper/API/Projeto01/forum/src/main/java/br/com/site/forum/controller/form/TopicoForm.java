package br.com.site.forum.controller.form;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.site.forum.modelo.Curso;
import br.com.site.forum.modelo.Topico;
import br.com.site.forum.repository.CursoRepository;


public class TopicoForm {
    @NotNull @NotEmpty @Length(min= 10)
    private String titulo;
    @NotNull @NotEmpty @Length(min= 10)
    private String mensagem;
    @NotNull @NotEmpty @Length(min= 10)
    private String nomeCurso;

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Topico converter(CursoRepository cursoRepository) {
        Curso curso =  cursoRepository.findByNome(nomeCurso);

        return new Topico(titulo,mensagem,curso);
    }
}
