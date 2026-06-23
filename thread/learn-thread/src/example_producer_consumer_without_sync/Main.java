package example_producer_consumer_without_sync;

import java.util.ArrayList;
import java.util.List;

public class Main {
  static final List<Integer> bucket = new ArrayList<>();

  public static void main(String[] args) {
    var p1 = new Producer("P1");
    var p2 = new Producer("P2");
    var c1 = new Consumer("C1");
    var c2 = new Consumer("C2");

    p1.start();
    p2.start();
    c1.start();
    c2.start();
  }
}
