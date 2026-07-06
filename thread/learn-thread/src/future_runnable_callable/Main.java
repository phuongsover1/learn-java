package future_runnable_callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(4);

        Runnable r = () -> System.out.println(":)"); // Ordering pizza

        Future<?> f = service.submit(r); // order a pizza and get back a receipt

        // do a lot of things while the pizza is being prepared

        try {
            f.get(); // get your pizza
            // if not ready -> then you have to wait for it
            // if ready -> get() return your pizza
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // pizza has been burnt
            // take a decision what you do next
        }

        service.shutdown();
    }
}
