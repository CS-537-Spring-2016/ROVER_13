package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;
import bot.graph.search.AStar;
import bot.location.Location;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by tj on 4/19/16.
 */
public class ExploreStrategy implements Strategy {
  public static final Logger logger = Logger.getLogger(ExploreStrategy.class.getName());
  private Random rng;
  private Deque<Node> queue;
  private Node target;
  private Node lastPosition;
  private int lastPositionCounter;

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
    if(roverStuck(start)){
      return randomMove();
    }
    if(target != null && !visited.contains(new Location(target.getX(), target.getY()))){
      return shortestPath(map, start, target);
    }


    List<Node> potentialNodes = dfs(map, start);
    Location location;
    for(Node node : potentialNodes){
      location = new Location(node.getX(), node.getY());
      if(!visited.contains(location)){
        queue.add(node);
      }
    }

    if(queue.size() < 1){
      return randomMove();
    }
    target = queue.remove();
    Location nextLoc = new Location(target.getX(), target.getY());
    while(queue.size() > 0 && visited.contains(nextLoc)){
      target = queue.remove();
    }
    logger.info("next target to move to: " + target);
    shortestPath(map, start, target);
    return randomMove();
  }

  private Direction shortestPath(Graph graph, Node start, Node end){
    AStar aStar = new AStar();
    List<Node> nodes = aStar.search(graph, start, end);
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

  private int updatePosition(Node start){
    if(lastPosition == null || !lastPosition.equals(start)){
      lastPosition = start;
      lastPositionCounter = 1;
    } else{
      lastPositionCounter++;
    }
    return lastPositionCounter;
  }

  private boolean roverStuck(Node start){
    int turnsInSameSpot = updatePosition(start);
    return turnsInSameSpot > 4;
  }
}
