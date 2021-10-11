package br.com.jrd.servidor;

import java.util.concurrent.ThreadFactory;

public class FabricaDeThreads implements ThreadFactory {
    static  int numero  =1 ;
    @Override
    public Thread newThread(Runnable r) {
        Thread thread  = new Thread(r,"Thread Servidor Taregas"+ numero);
        numero++;
        thread.setUncaughtExceptionHandler(new TratadorDeException());
        return thread;
    }

}
