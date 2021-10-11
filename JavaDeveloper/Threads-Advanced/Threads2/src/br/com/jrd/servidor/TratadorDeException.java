package br.com.jrd.servidor;

import java.lang.Thread.UncaughtExceptionHandler;

public class TratadorDeException implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        System.out.println("Deu Excecao na thread "+ t.getName() + "," + e.getMessage());
    }
    
}
