package bot.graph;

import java.util.*;

/**
 * Created by tj on 4/23/16.
 */
public class Graph {

  // each node has at most 4 edges, so using list
  // can reduce our memory footprint as opposed to hashset
  // contains method only has to check at most 4 nodes, O(1)
  public Map<Node, List<Node>> adjList;

  public Graph(){
    this.adjList = new HashMap<>();
  }

  public Graph(Graph copy){
    // use the contents of copy to create a new graph
  }

  // returns all the nodes that have an edge to node
  public List<Node> neighbors(Node node){
    return adjList.containsKey(node) ? adjList.get(node) : new ArrayList<>();
  }

  // hasEdgeTo
  public boolean adjacentTo(Node from, Node to){
    return neighbors(from).contains(to);
  }

  public Map<Node, List<Node>> getAdjacenyList(){
    return adjList;
  }

  // add node
  public boolean addNode(Node node){
    if(!adjList.containsKey(node)){
      adjList.put(node, new ArrayList<>());
      return true;
    }
    return false;
  }

  // remove node
  public boolean removeNode(Node node){
    if(adjList.containsKey(node)){
      // removeEdgeFromTo
      // iterate and remove all reference to node
      for(Map.Entry<Node, List<Node>> entry : adjList.entrySet()){
        entry.getValue().remove(node);
      }
      return adjList.remove(node) != null;
    }
    return false;
  }

  // adds an edge from 1st node to 2nd node
  public boolean addEdge(Node from, Node to){
    addNode(from);
    addNode(to);
    List<Node> edges = adjList.get(from);
    return !edges.contains(to) ? edges.add(to) : false;
  }

  // removes an edge from 1st node to 2nd node
  public boolean removeEdge(Node from, Node to){
    return adjList.containsKey(from) ? adjList.get(from).remove(to) : false;
  }

  // get nodes
  public Set<Node> getNodes(){
    return adjList.keySet();
  }

  public boolean contains(Node key){
    return adjList.containsKey(key);
  }
}
