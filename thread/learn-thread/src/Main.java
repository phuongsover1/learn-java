public class Main {
  public static void main(String[] args) throws InterruptedException {
    var r1 = new OddNumberRunnable();

    var t1 = new Thread(r1, "T1");
    var t2 = new Thread(r1, "T2");

    t1.start(); // NEW -> RUNNABLE
    t2.start(); // NEW -> RUNNABLE

    System.out.println("End " + Thread.currentThread().getName());
  }
}
