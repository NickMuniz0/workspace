package br.com.jrd.servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static void main(String[] args) throws Exception {

        System.out.println(" ----- Iniciando Servidor ----- ");

        // ExecutorService threadPool = Executors.newFixedThreadPool(4);//fixo
        ExecutorService threadPool = Executors.newCachedThreadPool();//dinamico

        while(true){

            Socket accept = new ServerSocket(12345).accept();
            System.out.println("Aceitando Novo Cliente na porta: " + accept.getPort());
            DistribuirTarefas distribuirTarefas = new DistribuirTarefas(accept);
            threadPool.execute(distribuirTarefas);//reaproveita thread

            // Thread threadCliente = new Thread(distribuirTarefas);//nao reaproveita
            // threadCliente.start();
        }

    }    
}
