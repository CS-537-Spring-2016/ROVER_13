package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;

import java.util.Random;

/**
 * Created by tj on 4/19/16.
 */
public class ShortestPathStrategy implements Strategy {

  private Random rng;

  public ShortestPathStrategy() {
    rng = new Random();
  }

  public Direction bestMove() {
    Direction[] directions = Direction.values();
    int choice = rng.nextInt(directions.length);
    return directions[choice];
  }

  @Override
  public Direction bestMove(Graph map, Node start) {
    return bestMove();
  }
}
