package future_runnable_callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(4);

        Callable<String> c = () -> "Hello";

        Future<String> f = service.submit(c);
        
        
    }
}
