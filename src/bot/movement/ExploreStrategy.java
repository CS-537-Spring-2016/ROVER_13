package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;
import bot.graph.search.AStar;
import bot.location.Location;

import java.util.*;

/**
 * Created by tj on 4/19/16.
 */
public class ExploreStrategy implements Strategy {

  private Random rng;
  private Deque<Node> queue;

  public ExploreStrategy() {
    rng = new Random();
    queue = new ArrayDeque<>();
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

  public Direction bestMove(Graph map, Node start, Set<Location> visited) {
    List<Node> potentialNodes = dfs(map, start);
    for(Node node : potentialNodes){
      if(!visited.contains(node)){
        queue.add(node);
      }
    }

    Node next = queue.remove();
    AStar aStar = new AStar();
    List<Node> nodes = aStar.search(map, start, next);
    Node nextBestNode;
    if(nodes.size() > 0){
      nextBestNode = nodes.get(0);
      return moveFromTo(start, nextBestNode);
    }
    return randomMove();
  }

  private List<Node> dfs(Graph graph, Node source){
    int numNodes = graph.getNodes().size();
    List<Node> result = new ArrayList<>(numNodes);
    Deque<Node> stack = new ArrayDeque<>();
    Set<Node> dfsVisit = new HashSet<>(numNodes);
    Node current = source;
    stack.push(current);
    while(stack.size() > 0){
      current = stack.pop();
      result.add(current);
      dfsVisit.add(current);
      for(Node adj : graph.neighbors(current)){
        if(!dfsVisit.contains(adj)){
          stack.push(adj);
        }
      }
    }
    return result;
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
