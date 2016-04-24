package bot.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tj on 4/23/16.
 */
public class Graph {

  public Map<Node, List<Node>> adjList;

  public Graph(){
    this.adjList = new HashMap<>();
  }

  public Graph(Graph copy){
    // use the contents of copy to create a new graph
  }


}
