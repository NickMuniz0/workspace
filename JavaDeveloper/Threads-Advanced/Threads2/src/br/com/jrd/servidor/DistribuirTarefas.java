package br.com.jrd.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DistribuirTarefas implements Runnable{

    private Socket socket;
    private Servidor servidor;
    private ExecutorService threadPool;

    DistribuirTarefas(ExecutorService threadPool, Socket socket, Servidor servidor){
        this.threadPool = threadPool;
        this.servidor = servidor;
        this.socket = socket;

    }

    @Override
    public void run() {
        try {

            System.out.println("Distribuindo Tarefas"+ socket);

            Scanner entradaCliente =  new Scanner(socket.getInputStream());
            PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
 
            while(entradaCliente.hasNextLine()){
                String comando = entradaCliente.nextLine();
                System.out.println("Comando Recebido "+comando);

                switch (comando) {
                    case "c1":
                        saidaCliente.println("confirmacao do comando c1");
                        ComandoC1 c1 = new ComandoC1(saidaCliente);
                        threadPool.execute(c1);
                        break;
                    case "fim":
                        saidaCliente.println("Desligando o servidor");
                        ComandoC3ChamaWS c3 = new ComandoC3ChamaWS(saidaCliente);
                        Future<String> futureWS = threadPool.submit(c3);
                        ComandoC1 c4 = new ComandoC1(saidaCliente);
                        this.threadPool.submit(new JuntaResultadosFutureWSFutureBanco(futureWS,c4,saidaCliente));
                        servidor.parar();
                        break;
                
                    default:
                        saidaCliente.println("confirmacao do comando default");

                        break;
                };



                System.out.println(comando);
            }
            saidaCliente.close();
            entradaCliente.close();

        } catch (Exception e) {
           throw new RuntimeException(e);
        }

        
    }

}
