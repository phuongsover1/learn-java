package thead_example_volatile;

public class Main {
  public static volatile int c = 0; // volatile is used to ensure that the value of c is read from the main memory
                                    // and not from the cache

  public static void main(String[] args) {

    var incrementThread = new IncrementThread();
    var printingThread = new PrintingThread();

    incrementThread.start();
    printingThread.start();
  }
}
