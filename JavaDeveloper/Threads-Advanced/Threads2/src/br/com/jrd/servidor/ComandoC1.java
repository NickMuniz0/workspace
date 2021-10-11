package br.com.jrd.servidor;

import java.io.PrintStream;

public class ComandoC1 implements Runnable {

    private PrintStream saida;

    public ComandoC1(PrintStream saida) {
        this.saida = saida;
    }

    @Override
    public void run() {
        System.out.println("Executando c1");        
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        throw new RuntimeException("exception no comando c2");
        // saida.println("Comando C1 executado com sucesso!");
    }


    


}
