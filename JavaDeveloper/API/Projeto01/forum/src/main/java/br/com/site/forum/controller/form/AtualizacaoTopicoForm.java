package br.com.site.forum.controller.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.site.forum.modelo.Topico;
import br.com.site.forum.repository.TopicoRepository;

import javax.validation.constraints.NotEmpty;


public class AtualizacaoTopicoForm {
    @NotNull @NotEmpty @Length(min= 10)
    private String titulo;
    @NotNull @NotEmpty @Length(min= 10)
    private String mensagem;
    public Topico atualizar(Long id, TopicoRepository topicoRepository) {
        Topico topico = topicoRepository.getById(id);
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);
        return topico;
    }

    
}

