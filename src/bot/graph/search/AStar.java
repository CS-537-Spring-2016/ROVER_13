package bot.graph.search;

import bot.graph.Graph;
import bot.graph.Node;

import java.util.*;

/**
 * Created by tj on 4/24/16.
 */
public class AStar {
  private final boolean DEBUG = false;
  public Graph graph;

  public AStar(){

  }

  private List<Node> execute(Graph graph, Node source, Node dest){
    Node destination = dest;
    graph.prepareSearcableNodes(dest);
    Node start = source;
    start.setShortestDistance(0);
    Node end = dest;

    PriorityQueue<Node> queue = new PriorityQueue<>();
    Set<Node> closed = new HashSet<>();
    queue.add(start);
    while(!queue.isEmpty() && queue.peek().f() <= end.f()){
      Node currentMeta = queue.remove();
      closed.add(currentMeta);
      List<Node> neighborNodes = graph.neighbors(currentMeta);
      List<Node> metas = neighborNodes;
      metas.forEach(meta -> {
        if(currentMeta.g() + 1 < meta.g()){
          // update the meta
          meta.setParent(currentMeta);
          meta.setShortestDistance(currentMeta.g() + 1);
          if(!queue.contains(meta) && !closed.contains(meta)){
              queue.add(meta);
          }
        }
      });
    }
    // FIXME
    return makePath(start, end);
  }

  public List<Node> search(Graph graph, Node source, Node dest) {
    return execute(new Graph(graph), new Node(source), new Node(dest));
  }

  public int minDistance(Graph graph, Node source, Node dest){
    // FIXME
    List<Node> path = execute(new Graph(graph), new Node(source), new Node(dest));
    int dist = path.size();
    return -1;
  }

  public List<Node> makePath(Node source, Node dest){
    List<Node> result = new ArrayList<>();
    Node current = dest;
    // TODO check if checking for reference is correct
    while( current != source){
      result.add(current);
      current = current.getParent();
    }
    return result;
  }
}
