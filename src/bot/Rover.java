package bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Rover {

  private String roverName;
  private final String SERVER_ADDRESS;
  private final int SERVER_PORT;
  private int sleepTime;

  private BufferedReader in;
  private PrintWriter out;

  public Rover() {
    roverName = "ROVER_13";
    SERVER_ADDRESS = "localhost";
    SERVER_PORT = 9537;
    sleepTime = 200;
  }

  private void run(){
    try(Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)){
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch(UnknownHostException unHostEx){
      System.err.printf("unable to connect to socket %s%n", unHostEx);
    } catch(IOException ioEx){
      System.err.printf("IO exception while creating socket %s%n", ioEx);
    }

  }


  public static void main(String[] args){
    System.out.printf("%s%n", "Hello World from Rover13");
  }
}
