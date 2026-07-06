package future_runnable_callable;

import java.util.concurrent.Callable;

public class CustomSummingNumbersCallable implements Callable<Integer> {
    private int x;
    private int y;

    public CustomSummingNumbersCallable(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Integer call() {
        return x + y;
    }
}
