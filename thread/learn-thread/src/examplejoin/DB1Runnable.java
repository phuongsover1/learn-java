package examplejoin;

public class DB1Runnable implements Runnable {
  @Override
  public void run() {
    try {
      System.out.println("DB1Runnable is starting" + " " + Thread.currentThread().getName());
      Thread.sleep(1000);
      System.out.println("DB1Runnable is done" + " " + Thread.currentThread().getName());
    } catch (InterruptedException e) {
      System.out.println("InterruptedException: " + e.getMessage() + " " + Thread.currentThread().getName());
    }
  }
}
