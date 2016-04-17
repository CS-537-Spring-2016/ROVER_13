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
      return;
    } catch(IOException ioEx){
      System.err.printf("IO exception while creating socket %s%n", ioEx);
      return;
    }

    try{
      serverHandshake();
    } catch(IOException ex){
      System.err.printf("unable to establish handshake with server: %s%n", ex);
      return;
    }

  }

  private void serverHandshake() throws IOException{
    String line;
    while(true){
      line = in.readLine();
      if (line.startsWith("SUBMITNAME")) {
        out.println(roverName); // This sets the name of this instance of a swarmBot
        return;
      }
    }
  }


  public static void main(String[] args){
    System.out.printf("%s%n", "Hello World from Rover13");
  }
}
