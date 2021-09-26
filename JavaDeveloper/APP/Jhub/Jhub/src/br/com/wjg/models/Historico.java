package br.com.wjg.models;

import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class Historico {
    private JTextField descricao;
    private JTextField arquivo;
    private JTextField localizacao;
    private JTextField observacao;
    private LocalDate dataDaSolicitacao;

    private Autor autor;

    private JComboBox<Sinistro> sinistro;
	

    //Get
    public JTextField getArquivo() {
        return arquivo;
    }
    public JTextField getDescricao() {
        return descricao;
    }
    public JTextField getLocalizacao() {
        return localizacao;
    }
    public JTextField getObservacao() {
        return observacao;
    }
    public LocalDate getDataDaSolicitacao() {
        return dataDaSolicitacao;
    }
    public Autor getAutor() {
        return autor;
    }
    public JComboBox<Sinistro> getSinistro() {
        return sinistro;
    }
    //Set
    public void setArquivo(JTextField arquivo) {
        this.arquivo = arquivo;
    }
    public void setDescricao(JTextField descricao) {
        this.descricao = descricao;
    }
    public void setLocalizacao(JTextField localizacao) {
        this.localizacao = localizacao;
    }
    public void setObservacao(JTextField observacao) {
        this.observacao = observacao;
    }
    public void setDataDaSolicitacao(LocalDate dataDaSolicitacao) {
        this.dataDaSolicitacao = dataDaSolicitacao;
    }
    public void setAutor(Autor autor) {
        this.autor = autor;
    }
    public void setSinistro(JComboBox<Sinistro> jtsinistro) {
        this.sinistro = jtsinistro;
    }
  
}
