package example_producer_consumer_without_sync;

public class Consumer extends Thread {
  Consumer(String name) {
    super(name);
  }

  @Override
  public void run() {
    try {
      while (true) {
        synchronized (Main.bucket) { // decide which is our monitor
          if (!Main.bucket.isEmpty()) {
            int number = Main.bucket.get(0);
            Main.bucket.remove(0);
            Main.bucket.notifyAll();
            System.out.println(number + " was consumed by " + Thread.currentThread().getName());
          } else {
            Main.bucket.wait();
          }
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // not sync
  }
}
