package bot.graph.search;

import bot.graph.Graph;
import bot.graph.Node;

import java.util.List;

/**
 * Created by tj on 4/24/16.
 */
public class AStar {
  private final boolean DEBUG = false;
  public Graph graph;

  public AStar(){

  }

  private List<Node> execute(Graph graph, Node source, Node dest){
    return null;
  }

  public List<Node> search(Graph graph, Node source, Node dest) {
    List<Node> path = execute(new Graph(graph), new Node(source), new Node(dist));
    return null;
  }

  public int minDistance(Graph graph, Node source, Node dest){
    List<Node> path = execute(new Graph(graph), new Node(source), new Node(dist));
    int dist = path.size();
    return -1;
  }
}
