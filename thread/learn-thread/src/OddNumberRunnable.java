public class OddNumberRunnable implements Runnable {

  @Override
  public void run() {
    for (int i = 1; i < 10; i += 2) {
      System.out.println(i + " " + Thread.currentThread().getName());
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        System.out.println("InterruptedException: " + e.getMessage());
      }
    }
  }

}
