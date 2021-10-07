package br.com.wjg.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteGit implements Runnable {

    private String[] comandos;

    public ExecuteGit(String... comandos){
        this.comandos = comandos;

    }

    @Override
    public void run() {
        execute(comandos);
        
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


    @Override
    public String toString() {
        return execute(comandos).toString();
    }

}
