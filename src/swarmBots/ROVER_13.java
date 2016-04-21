package swarmBots;

import bot.Rover;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ROVER_13 {
  public static void main(String[] args){
    Rover.logger.setLevel(Level.ALL);
    Rover rover = new Rover();
    rover.run();
  }
}
