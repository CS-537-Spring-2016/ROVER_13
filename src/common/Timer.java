package common;

/*
 * Timer class for measuring time of any method
 * 
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
		System.out.println("ElapsedTime: " +difference);
		return difference;
	}

}
