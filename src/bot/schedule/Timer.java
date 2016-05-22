package bot.schedule;

/**
 * Created by tj on 5/7/16.
 * base provided by mitali
 */
public class Timer {

  //start time and endtime attributes
  long startTime;
  long stopTime;
  long difference;

  //start function to record the current time in milliseconds
  public void start(){
    startTime = System.currentTimeMillis();
  }
  //returns the difference in milliseconds that have elapsed since last start call
  public long stop(){
    stopTime = System.currentTimeMillis();
    difference = stopTime - startTime;

    return difference;
  }

  //getters only
  public long getStartTime() {
    return startTime;
  }
  public long getStopTime() {
    return stopTime;
  }
  public long getDifference() {
    return difference;
  }




}