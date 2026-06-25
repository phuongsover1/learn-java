package example_producer_consumer_without_sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Producer extends Thread {

  Producer(String name) {
    super(name);
  }

  @Override
  public void run() {
    Random r = new Random();
    try {
      while (true) {
        synchronized (Main.bucket) {
          if (Main.bucket.size() < 100) {
            int number = r.nextInt(1000);
            Main.bucket.add(number);
            Main.bucket.notifyAll();
            System.out.println(number + "  was added to bucket");
          } else {
            Main.bucket.wait(); // producer is waiting for the consumer to consume the items
          }
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

}
