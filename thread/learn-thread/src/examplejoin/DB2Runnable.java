package examplejoin;

public class DB2Runnable implements Runnable {

  @Override
  public void run() {
    try {
      System.out.println("DB2 is starting" + " " + Thread.currentThread().getName());
      Thread.sleep(1000);
      System.out.println("DB2 is done" + " " + Thread.currentThread().getName());
    } catch (InterruptedException e) {
      System.out.println("InterruptedException: " + e.getMessage() + " " + Thread.currentThread().getName());
    }
  }

}
