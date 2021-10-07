package br.com.wjg.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

public class ExecuteGit implements Runnable {

    private String[] comandos;
    private JTextArea result;

    public ExecuteGit(String... comandos){
        this.comandos = comandos;

    }

    @Override
    public void run() {
        StringBuilder execute = execute(comandos);
        result.setText( execute.toString()); 
        if(execute.toString().substring(0,5).equals("fatal")) { new Mensagens(execute.toString()); return; }
        new Mensagens("Arquivo enviado para o repositorio!");
       
    }
     
    public StringBuilder execute(String... comandos){
        StringBuilder saida = new StringBuilder();
        BufferedReader leitor;
        ProcessBuilder processos;
        Process processo;
        try {

                processos = new ProcessBuilder("cmd.exe", "/c", String.join(" && ", comandos));   
                processos.redirectErrorStream(true);
                processo = processos.start();
                processo.waitFor();
                leitor = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                String linha = "";

                while ((linha = leitor.readLine()) != null) {
                  saida.append(linha).append("\n");
                } 
                
                
        } catch (IOException e1) {
                new Mensagens(e1.getMessage());
        } catch (InterruptedException e1) {
            	new Mensagens(e1.getMessage());
        }
        
        return saida;

    }


    public void write(JTextArea result){
        this.result = result;        
       
    }




}
