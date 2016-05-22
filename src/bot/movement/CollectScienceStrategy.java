package bot.movement;

import bot.graph.Graph;
import bot.graph.Node;
import bot.graph.search.AStar;
import bot.location.Location;
import enums.Terrain;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by tj on 4/19/16.
 */
public class CollectScienceStrategy {
  public static final Logger logger = Logger.getLogger(CollectScienceStrategy.class.getName());
  private Random rng;
  private Deque<Node> path;
  private Deque<Node> targetsStack;
  private Node target;
  private Node lastPosition;
  private int lastPositionCounter;

  public CollectScienceStrategy() {
    rng = new Random();
    path = new ArrayDeque<>();
    targetsStack = new ArrayDeque<>();
  }

  public Direction bestMove() {
    Direction[] directions = Direction.values();
    int choice = rng.nextInt(directions.length);
    return directions[choice];
  }

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

  public Direction bestMove(Graph map, Node start, Node target, Set<Location> visited) {
    if(roverStuck(start)) {
      // push the old target on the stack,
      // pick a new target from the old path
      // create a path to the new target
      // replace the path with the new path
      return shortestPath(map, start, randomPreviousVisited(visited));
    }

    // check if we have a path to travel still
    if(path.size() > 0 ){
      Node next = path.remove();
      try{
        return moveFromTo(start, next);
      } catch(IllegalArgumentException ex){
        List<Node> nodes  = new AStar().search(map, start, target);
        this.target = target;
        path = new ArrayDeque<>();
        path.addAll(nodes);
        Node nextBestNode;
        if(path.size() > 0){
          nextBestNode = path.remove();
          return moveFromTo(start, nextBestNode);
        }
        return shortestPath(map, start, randomPreviousVisited(visited));
      }

    } else {
      List<Node> nodes  = new AStar().search(map, start, target);
      this.target = target;
      path = new ArrayDeque<>();
      path.addAll(nodes);
      Node nextBestNode;
      if(path.size() > 0){
        nextBestNode = path.remove();
        return moveFromTo(start, nextBestNode);
      }
      return shortestPath(map, start, randomPreviousVisited(visited));
    }
  }

  private Direction shortestPath(Graph graph, Node start, Node end){
    AStar aStar = new AStar();
    logger.info("shortest path from: " + start + " to " + end);
    List<Node> nodes = aStar.search(graph, start, end);
    Node nextBestNode;
    if(nodes.size() > 0){
      nextBestNode = nodes.get(0);
      return moveFromTo(start, nextBestNode);
    }
    // random adjacent
    List<Node> adjacentNodes = new ArrayList<>();
    for(Node node : graph.neighbors(start)){
      if(node.getTerrain() != Terrain.NONE && node.getTerrain() != Terrain.ROCK){
        adjacentNodes.add(node);
      }
    }
    if(adjacentNodes.size() > 0){
      int choice = rng.nextInt(adjacentNodes.size());
      Node next = adjacentNodes.get(choice);
      logger.info("moving to a random adjacent to: " + next);
      return moveFromTo(start, next);
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
    Direction direction = directions[choice];
    logger.info("randomly moving to: " + direction);
    return direction;
  }

  private Node randomPreviousVisited(Set<Location> visited) {
    int choice = rng.nextInt(visited.size());
    Iterator<Location> it = visited.iterator();
    Location loc = it.next();
    for(int i = 1; i < choice; i++){
      if(it.hasNext()){
        loc = it.next();
      }
    }
    Node node = new Node(loc.getX(), loc.getY());
    return node;
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
