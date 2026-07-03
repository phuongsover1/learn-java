package executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int n = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = // pool of threads
                Executors.newFixedThreadPool(n); // fixed size thread pool
        try {

            Runnable r = () -> System.out.println(":) " +
                    Thread.currentThread().getName()); // ball

            executor.execute(r); // throwing ball to the pool
            // executor.shutdown();

            System.out.println(":( " + Thread.currentThread().getName());

        } finally {
            executor.shutdown();
            // executor.shutdownNow();
        }
    }

}
