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
public class ExploreStrategy implements Strategy {
  public static final Logger logger = Logger.getLogger(ExploreStrategy.class.getName());
  private Random rng;
  private PriorityQueue<Node> queue;
  private Node target;
  private Node lastPosition;
  private int lastPositionCounter;

  public ExploreStrategy() {
    rng = new Random();
    queue = new PriorityQueue<>(new Comparator<Node>() {
      @Override
      public int compare(Node thisNode, Node thatNode){
        int thisX = thisNode.getX();
        int thisY = thisNode.getY();
        int thatX = thatNode.getX();
        int thatY = thatNode.getY();
        return (int) (Math.sqrt(thatX*thatX + thatY*thatY) - Math.sqrt(thisX*thisX + thisY*thisY));
      }
    });
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
      try{
        logger.warning("stuck, trying to move around");
        List<Node> nodes = new AStar().search(map, start, target);
        map.removeNode(nodes.get(0));
        nodes = new AStar().search(map, start, target);
        return shortestPath(map, start, target);
      } catch(Exception ex){
        return shortestPath(map, start, randomPreviousVisited(visited));
      }


    }
    if(target != null && !visited.contains(new Location(target.getX(), target.getY()))){
      return shortestPath(map, start, target);
    }

    List<Node> potentialNodes = bfs(map, start);
    Location location;
    for(Node node : potentialNodes){
      location = new Location(node.getX(), node.getY());
      if(!visited.contains(location)){
        queue.add(node);
      }
    }

    if(queue.size() < 1){
      return shortestPath(map, start, randomPreviousVisited(visited));
    }

    target = queue.remove();
    Location nextLoc = new Location(target.getX(), target.getY());
    while(queue.size() > 0 && visited.contains(nextLoc)){
      target = queue.remove();
      nextLoc = new Location(target.getX(), target.getY());
    }
    logger.info("queue has number of elements: " + queue.size());
    logger.info("visited size: " + visited.size());
    logger.info("graph nodes size: " + map.getNodes().size());
    logger.info("next target to move to: " + target);
    return shortestPath(map, start, target);
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

  private List<Node> bfs(Graph graph, Node source){
    int numNodes = graph.getNodes().size();
    List<Node> result = new ArrayList<>(numNodes);
    Deque<Node> queue = new ArrayDeque<>();
    Set<Node> bfsVisit = new HashSet<>(numNodes);
    queue.add(source);
    while(queue.size() > 0){
      Deque<Node> frontier = new ArrayDeque<>();
      for(Node node : queue){
        if(!bfsVisit.contains(node)){
          for(Node adj : graph.neighbors(node)){
            if(!bfsVisit.contains(adj)){
              frontier.add(adj);
            }
          }
          bfsVisit.add(node);
          result.add(node);
        }
      }
      queue = frontier;
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
