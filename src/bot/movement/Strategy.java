package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;
import bot.location.Location;

import java.util.Set;

/**
 * Created by tj on 4/19/16.
 */
public interface Strategy {

  public Direction bestMove();
  public Direction bestMove(Graph map, Node start, Node end);
  public Direction bestMove(Graph map, Node start, Set<Location> visited);
}
