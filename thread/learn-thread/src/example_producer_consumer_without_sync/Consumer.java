package example_producer_consumer_without_sync;

public class Consumer extends Thread {
  Consumer(String name) {
    super(name);
  }

  @Override
  public void run() {
    while (true) { // C1 [10] C2 [10]
      if (!Main.bucket.isEmpty()) { // C1 [10] C2 [10]
        int number = Main.bucket.get(0); // C1 [10] -> C1 (Runnable state)
        Main.bucket.remove(0); // C2 [] -> C2 (Runnable state) -> C1 throw Exception
        System.out.println(number + " was consumed by " + Thread.currentThread().getName());
      }
    }
  }
}
