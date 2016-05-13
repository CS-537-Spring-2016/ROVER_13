package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;
import bot.graph.search.AStar;

import java.util.List;
import java.util.Random;

/**
 * Created by tj on 4/19/16.
 */
public class ExploreStrategy implements Strategy {

  private Random rng;

  public ExploreStrategy() {
    rng = new Random();
  }

  public Direction bestMove() {
    Direction[] directions = Direction.values();
    int choice = rng.nextInt(directions.length);
    return directions[choice];
  }

  @Override
  public Direction bestMove(Graph map, Node start, Node end) {
    AStar aStar = new AStar();
    List<Node> nodes = aStar.search(map, start, end);
    Node nextBestNode;
    if(nodes.size() > 0){
      nextBestNode = nodes.get(0);
      return moveFromTo(start, nextBestNode);
    }
    return randomMove();
  }

  // precondition: from and to must be adjacent; otherwise, throw illegalarugmentexception
  private Direction moveFromTo(Node from, Node to){
    int xDiff = from.getX() - to.getX();
    int yDiff = from.getY() - to.getY();

    if(xDiff == 0 && yDiff == 1){
      return Direction.NORTH;
    }
    if(xDiff == 0 && yDiff == -1){
      return Direction.SOUTH;
    }
    if(xDiff == -1 && yDiff == 0){
      return Direction.EAST;
    }
    if(xDiff == 1 && yDiff == 0){
      return Direction.WEST;
    }
    throw new IllegalArgumentException("nodes are not adjacent " + "from:" + from + " to: " + to);
  }

  private Direction randomMove() {
    Direction[] directions = Direction.values();
    int choice = rng.nextInt(directions.length);
    return directions[choice];
  }
}
