package br.com.jrd.servidor;

import java.io.PrintStream;

public class ComandoC2 implements Runnable {

    private PrintStream saida;

    public ComandoC2(PrintStream saida) {
        this.saida = saida;
    }

    @Override
    public void run() {
        System.out.println("Executando c2");        
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        saida.println("Comando C2 executado com sucesso!");
    }


    


}
