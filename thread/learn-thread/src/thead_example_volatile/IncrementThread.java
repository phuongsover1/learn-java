package thead_example_volatile;

public class IncrementThread extends Thread {

  @Override
  public void run() {
    Main.c++;
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
