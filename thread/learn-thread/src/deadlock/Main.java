package deadlock;

public class Main {
  private Object o1 = new Object();
  private Object o2 = new Object();

  private void method1() {
    // T3
    synchronized (o1) {
      // T1
      synchronized (o2) { // DEADLOCK
        System.out.println("Done processing method1");
      }
    }
  }

  private void method2() {
    // T4
    synchronized (o2) {
      // T2
      synchronized (o1) { // DEADLOCK
        System.out.println("Done processing method2");
      }
    }
  }

  public static void main(String[] args) {
    var main = new Main();
    var t1 = new Thread(main::method1);
    var t2 = new Thread(main::method2);
    t1.start();
    t2.start();
  }
}
