package br.com.site.forum.controller.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.site.forum.modelo.StatusTopico;
import br.com.site.forum.modelo.Topico;

public class DetalhesTopicoDto {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private String nomeAutor;
    private StatusTopico status;
    private List<RespostaDto> resportas;

    public DetalhesTopicoDto(Topico topico){
        this.id =topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
        this.nomeAutor = topico.getAutor().getNome();
        this.status = topico.getStatus();
        this.resportas= new ArrayList<>();
        this.resportas.addAll(topico.getRespostas().stream().map(RespostaDto::new).collect(Collectors.toList()));
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }public Long getId() {
        return id;
    }public String getMensagem() {
        return mensagem;
    }public String getNomeAutor() {
        return nomeAutor;
    }public List<RespostaDto> getResportas() {
        return resportas;
    }public StatusTopico getStatus() {
        return status;
    }public String getTitulo() {
        return titulo;
    }


}
