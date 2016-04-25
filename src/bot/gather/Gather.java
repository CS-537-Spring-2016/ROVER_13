package bot.gather;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class Gather 
{
	
	 private BufferedReader in;
	  private PrintWriter out;
	  
	@SuppressWarnings("unused")
	private boolean gather()
	  {
		
		//Get current location and Gather
	    //TODO
	    out.println("GATHER");
	    String response;
	    try
	    {
	      response = in.readLine();
	      // parse the response into an object
	      // use the object to build a new map/graph
	      return true;
	    } 
	    catch(IOException ex)
	    {
	      System.err.println("exception reading outputstream during Gather " + ex);
	      return false;
	    }

	  }
}
