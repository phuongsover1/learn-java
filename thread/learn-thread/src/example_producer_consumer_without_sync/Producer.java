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

    while (true) {
      synchronized (Main.bucket) {
        if (Main.bucket.size() < 100) {
          int number = r.nextInt(1000);
          Main.bucket.add(number);
          System.out.println(number + "  was added to bucket");
        }
      }
    }
  }

}
