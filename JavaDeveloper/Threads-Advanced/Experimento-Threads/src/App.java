import java.util.concurrent.atomic.AtomicBoolean;

public class App {
    // private boolean estaRodando = false; warning concorrencia 
    // private volatile boolean estaRodando = false; volatile
    private AtomicBoolean  estaRodando = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        App servidor = new App();
        servidor.rodar();
        servidor.alterandoAtributo();
    }

    private void rodar() {
        new Thread(new Runnable() {

            public void run() {
                System.out.println("Servidor começando, estaRodando = " + estaRodando );

                // while(!estaRodando) {} volatile
                while(!estaRodando.get()) {}

                System.out.println("Servidor rodando, estaRodando = " + estaRodando );

                // while(estaRodando) {} volatile
                while(estaRodando.get()) {} 
                System.out.println("Servidor terminando, estaRodando = " + estaRodando );
            }
        }).start();
    }

    private void alterandoAtributo() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("Main alterando estaRodando = true");
        // estaRodando = true; volatile
        this.estaRodando.set(true);

        Thread.sleep(5000);
        System.out.println("Main alterando estaRodando = false");
        // estaRodando = false;   volatile
        this.estaRodando.set(false);

    }
}
