package bot.schedule;

/**
 * Created by mitali on 5/7/16
 */
/*
 * This class will tell the running time in milliseconds of any function by
 * using two functions that are start() and stop()
 */
public class Timer {

	// start time and endtime attributes
	long startTime;
	long stopTime;
	long difference;

	// start function to record the current time in milliseconds
	public void start() {
		startTime = System.currentTimeMillis();
	}

	// returns the difference in milliseconds that have elapsed since last start
	// call
	public long stop() {
		stopTime = System.currentTimeMillis();
		difference = stopTime - startTime;

		return difference;
	}

	// getters only
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