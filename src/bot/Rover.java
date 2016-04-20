package bot;

import bot.movement.Direction;
import bot.movement.RandomStrategy;
import bot.movement.Strategy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rover {

  // communication related
  private static final Logger logger = Logger.getLogger(Rover.class.getName());
  private String roverName;
  private String SERVER_ADDRESS;
  private int SERVER_PORT;
  private int sleepTime;

  private BufferedReader in;
  private PrintWriter out;

  // knowledge
  private Strategy strategy;

  public Rover() {
    this("localhost");
  }

  public Rover(String serverAddress) {
    roverName = "ROVER_13";
    SERVER_ADDRESS = serverAddress;
    SERVER_PORT = 9537;
    // FIXME: implement class to cover the sleep time
    sleepTime = 1200;
    strategy = new RandomStrategy();
  }

  private void run(){
    try{
      Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch(UnknownHostException unHostEx){
      System.err.printf("unable to connect to socket %s%n", unHostEx);
      unHostEx.printStackTrace();
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

    // rover is alive and communication
    logger.log(Level.INFO, "rover_13 is up");
    while(true){
      // TODO do interesting rover things
      //getLocation();
      //getScan();
      /*
      logic for MOVE, GATHER
      */
      waitUntilReady();
      makeBestMove();
    }

  }

  private void waitUntilReady(){
    try{
      Thread.sleep(sleepTime);
    } catch(InterruptedException ex){
      ex.printStackTrace();
    }
  }

  private void makeBestMove(){
    // decide what direction to move in
    Direction direction = strategy.bestMove();
    move(direction);
  }

  private void move(Direction direction){
    logger.info("moving direction: " + direction);
     out.println("MOVE " + direction.getAbbrev());
  }

  private boolean scan(){
    // TODO
    out.println("SCAN");
    String response;
    try{
      response = in.readLine();
      // parse the response into an object
      // use the object to build a new map/graph
      return true;
    } catch(IOException ex){
      System.err.println("exception reading outputstream during scan " + ex);
      return false;
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
    Rover.logger.setLevel(Level.ALL);
    Rover rover = new Rover();
    rover.run();
  }
}
