public class EvenNumberThead extends Thread {

  @Override
  public void run() {
    for (int i = 0; i < 10; i += 2) {
      try {
        System.out.println(i + " " + Thread.currentThread().getName());
        Thread.sleep(1_000);
      } catch (InterruptedException e) {
        System.out.println("InterruptedException: " + e.getMessage());
      }
    }
  }

}
