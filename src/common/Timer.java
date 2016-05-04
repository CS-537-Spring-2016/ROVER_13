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
	
	

}
