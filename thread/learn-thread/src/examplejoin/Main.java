package examplejoin;

public class Main {
  public static void main(String[] args) {
    var r1 = new DB1Runnable();
    var r2 = new DB2Runnable();

    var t1 = new Thread(r1, "T1");
    var t2 = new Thread(r2, "T2");

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("PROCESSING DATA AFTER DB1 AND DB2 " + Thread.currentThread().getName());
  }
}
