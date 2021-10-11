import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TesteFila {
    public static void main(String[] args) throws InterruptedException , InterruptedException {
        BlockingQueue<String> fila = new ArrayBlockingQueue(3);
        // fila.offer("c1");
        // fila.offer("c2");
        // fila.offer("c3");

        fila.put("c1");
        fila.put("c2");
        fila.put("c3");
        fila.put("c4");


        fila.take();
        fila.take();
        fila.take();
        fila.take();


         System.out.println(fila.size());

    }
    
}
