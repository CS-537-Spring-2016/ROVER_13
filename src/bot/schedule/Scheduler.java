package bot.schedule;

/**
 * Created by tj on 5/12/16.
 */
public class Scheduler {
  private Timer timer;

  public Scheduler(){
    timer = new Timer();
  }

  public void logLastMove(){
    timer.start();
  }

  public void scheduleNextMoveReady(){
    long error = 50;
    long elapsed = timer.stop();
    long cooldown = 1200L;
    long remaining = cooldown - elapsed;
    if(remaining + error > 0){
      try {
        Thread.sleep(remaining);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }
}
