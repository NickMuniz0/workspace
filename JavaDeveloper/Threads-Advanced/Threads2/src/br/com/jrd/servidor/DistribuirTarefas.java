package br.com.jrd.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable{

    private Socket socket;
    private Servidor servidor;

    DistribuirTarefas(Socket socket, Servidor servidor){
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
                        break;
                    case "fim":
                        saidaCliente.println("Desligando o servidor");
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
