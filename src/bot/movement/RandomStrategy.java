package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;
import bot.location.Location;

import java.util.Random;
import java.util.Set;

/**
 * Created by tj on 4/19/16.
 */
public class RandomStrategy implements Strategy {

  private Random rng;

  public RandomStrategy() {
    rng = new Random();
  }

  public Direction bestMove() {
    Direction[] directions = Direction.values();
    int choice = rng.nextInt(directions.length);
    return directions[choice];
  }

  @Override
  public Direction bestMove(Graph map, Node start, Node end) {
    return bestMove();
  }

  public Direction bestMove(Graph map, Node start, Set<Location> visited){
    return bestMove();
  }
}
