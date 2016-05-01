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
  private Map<Node, NodeMeta> map;

  public AStar(){
    map = new HashMap<>();
  }

  private List<Node> execute(Graph graph, Node source, Node dest){
    NodeMeta start = map.get(source);
    start.setShortestDistance(0);
    NodeMeta end = map.get(dest);

    Queue<NodeMeta> queue = new PriorityQueue<>();
    Set<Node> closed = new HashSet<>();
    queue.add(start);
    while(!queue.isEmpty() && queue.peek().f() <= end.f()){
      NodeMeta currentMeta = queue.remove();
      closed.add(currentMeta.getmNode());
      List<Node> neighborNodes = graph.neighbors(currentMeta.getmNode());
      List<NodeMeta> metas = new ArrayList<>();
      neighborNodes.forEach(key -> {
        metas.add(map.get(key));
      });
      metas.forEach(meta -> {
        if(currentMeta.g() + 1 < meta.g()){
          // update the meta
          meta.setParent(currentMeta.getmNode());
          meta.setShortestDistance(currentMeta.g() + 1);
          final double distanceAway = meta.f();
          if(!queue.contains(meta) && !closed.contains(meta.getmNode())){
            queue.add(map.get(meta.getmNode()));
          }
        }
      });
    }
    return makePath(map, graph, start.getmNode(), end.getmNode());
  }

  public List<Node> search(Graph graph, Node source, Node dest) {
    if(source.equals(dest) ||
            !graph.getNodes().contains(source) ||
            !graph.getNodes().contains(dest)){
      return new ArrayList<>();
    }
    map = new HashMap<>();
    for(Node node : graph.getNodes()){
      map.put(node, new NodeMeta(node, node.getX(), node.getY()));
    }
    NodeMeta destination = map.get(dest);
    map.keySet().forEach( key -> {
      map.get(key).setDestination(destination);
    });
    return execute(graph, source, dest);
  }

  public int minDistance(Graph graph, Node source, Node dest){
    List<Node> path = search(graph, source, dest);
    return path.size();
  }

  public List<Node> makePath(Map<Node, NodeMeta> map, Graph graph, Node source, Node dest){
    Deque<Node> deque = new ArrayDeque<>();
    NodeMeta current = map.get(dest);
    while( current.getmNode() != source){
      Node parent = current.getParent();
      Node child = current.getmNode();
      deque.addFirst(child);
      current = map.get(parent);
    }
    List<Node> result = new ArrayList<>();
    result.addAll(deque);
    return result;
  }

  private class NodeMeta implements Comparable{
    private final Node mNode;
    private Node parent;
    private NodeMeta destination;
    private int shortestDistance;
    private final int x;
    private final int y;

    public NodeMeta(Node mNode, int x, int y){
      this.mNode = mNode;
      this.parent = null;
      this.destination = null;
      this.shortestDistance = Integer.MAX_VALUE;
      this.x = x;
      this.y = y;
    }

    public Node getParent() {
      return parent;
    }

    public void setParent(Node parent) {
      this.parent = parent;
    }

    public int getShortestDistance() {
      return shortestDistance;
    }

    public void setShortestDistance(int shortestDistance) {
      this.shortestDistance = shortestDistance;
    }

    public Node getmNode() {
      return mNode;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    public NodeMeta getDestination() {
      return destination;
    }

    public void setDestination(NodeMeta destination) {
      this.destination = destination;
    }

    public double h(NodeMeta destination){
      int toX = destination.getX();
      int toY = destination.getY();
      return Math.sqrt(Math.pow(this.x - toX, 2) + Math.pow(this.y - toY, 2));
    }

    public int g(){
      return this.shortestDistance;
    }

    public double f(){
      return h(destination) + g();
    }

    @Override
    public int compareTo(Object other) {
      NodeMeta otherNode = (NodeMeta)other;
      double diff =(this.f() - otherNode.f())*10000;
      return (int)diff;
    }

    @Override
    public String toString(){
      return String.valueOf(this.mNode.toString());
    }

  }
}
