package br.com.jrd.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {


    private ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean estaRodando;

    public Servidor() throws IOException{
        System.out.println(" ----- Iniciando Servidor ----- ");

        // ExecutorService threadPool = Executors.newFixedThreadPool(4);//fi xo
        this.threadPool = Executors.newCachedThreadPool();//dinamico
        this.serverSocket = new ServerSocket(12345);
        this.threadPool = Executors.newFixedThreadPool(4,new FabricaDeThreads());
        this.estaRodando= true;
    }

    public void rodar() throws IOException {
        while(this.estaRodando){
            try {
                Socket accept = serverSocket.accept();
                System.out.println("Aceitando Novo Cliente na porta: " + accept.getPort());
                DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool,accept, this);
                threadPool.execute(distribuirTarefas);//reaproveita thread
                // Thread threadCliente = new Thread(distribuirTarefas);//nao reaproveita
                // threadCliente.start();
            } catch (SocketException e) {
                System.out.println("SocketException, Esta rodando?"+this.estaRodando);
            }
           


        }
    }  
    public void parar() throws IOException {
        estaRodando= false;
        serverSocket.close();
        threadPool.shutdown();
    }

    public static void main(String[] args) throws Exception {


        Servidor servidor = new Servidor();
        servidor.rodar();
        servidor.parar();



     
        




    }

   

    
}
