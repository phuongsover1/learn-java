package thead_example_volatile;

public class PrintingThread extends Thread {

  @Override
  public void run() {
    System.out.println(Main.c);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
