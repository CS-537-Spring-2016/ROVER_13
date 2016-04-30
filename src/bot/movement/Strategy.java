package bot.movement;

/**
 * Created by tj on 4/19/16.
 */
public interface Strategy {

  public Direction bestMove();
//  public Direction bestMove(Graph map, Location roverLoc);
}
