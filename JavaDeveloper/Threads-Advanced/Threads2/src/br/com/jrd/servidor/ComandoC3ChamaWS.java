package br.com.jrd.servidor;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

public class ComandoC3ChamaWS implements Callable<String> {

    private PrintStream saida;

    public ComandoC3ChamaWS(PrintStream saida) {
        this.saida = saida;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Executando c3 - Banco ");        
        Thread.sleep(25000); 
        int numero = new Random().nextInt(100) + 1;
        System.out.println("Servidor finalizou comando c3 - Banco");      
        return Integer.toString(numero);
    }


    


}
