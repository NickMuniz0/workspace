package br.com.jrd.servidor;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JuntaResultadosFutureWSFutureBanco implements Callable<Void>  {

    private Future<String> resultado;
    private ComandoC1 c4;
    private PrintStream saidaCliente;

    public JuntaResultadosFutureWSFutureBanco(Future<String> resultado, ComandoC1 c4, PrintStream saidaCliente) {
        this.resultado = resultado;
        this.c4 = c4;
        this.saidaCliente = saidaCliente;
    }

    @Override
    public Void call()  {
        System.out.println("Aguardando resultados do future ");
        try {
            String numeroMagico = this.resultado.get(15,TimeUnit.SECONDS);
            this.saidaCliente.println("Resultado comando c3"+numeroMagico+ "");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Cancelando");
            this.saidaCliente.println("Execucao do c3 - TimeOut");
            this.resultado.cancel(true);
        }
        System.out.println(" Finalizou JuntaResutladoFutureWSFutureBanco");
        return null;
    }

}
