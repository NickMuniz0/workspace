package br.com.wjg.config;

import javax.swing.JTextField;

public class Git {

    private JTextField pathRespositorio;
    private JTextField branch;
    private JTextField comentario;
    private JTextField arquivo;
    private String ext = ".json";
    

    public String getExt() {
        return ext;
    }

    public JTextField getPathRespositorio() {
        return pathRespositorio;
    }
    public JTextField getArquivo() {
        return arquivo;
    }
    public JTextField getBranch() {
        return branch;
    }
    public JTextField getComentario() {
        return comentario;
    }
    
    public void setPathRespositorio(JTextField repositorio) {
        this.pathRespositorio = repositorio;
    }
    public void setArquivo(JTextField arquivo) {
        this.arquivo = arquivo;
    }
    public void setBranch(JTextField branch) {
        this.branch = branch;
    }
    public void setComentario(JTextField comentario) {
        this.comentario = comentario;
    }
    
    

    
}
