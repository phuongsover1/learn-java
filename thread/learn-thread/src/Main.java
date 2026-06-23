public class Main {
  public static void main(String[] args) throws InterruptedException {
    Thread t = new Thread(
        () -> {
          try {
            System.out.println("before sleep: " + Thread.currentThread().getState());
            Thread.sleep(10_000); // TIMED_WAITING while sleeping
            System.out.println("sleep finished normally"); // won't run if interrupted
          } catch (InterruptedException e) {
            System.out.println("interrupted during sleep!"); // false
            System.out.println(
                "isInterrupted: " + Thread.currentThread().isInterrupted()); // false
          }
          System.out.println("thread continues...");
        });

    t.start();
    // Thread.sleep(100); // ensure the thread is sleeping
    t.interrupt();
  }
}
