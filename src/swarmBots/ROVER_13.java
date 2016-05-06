package swarmBots;

import bot.Rover;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ROVER_13 {
  public static void main(String[] args){
    Rover.logger.setLevel(Level.ALL);
    Rover rover = (args.length == 1) ? new Rover(args[0]) : new Rover();
    rover.run();
  }
}
