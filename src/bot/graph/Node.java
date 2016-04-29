package bot.graph;

import enums.Science;
import enums.Terrain;

/**
 * Created by tj on 4/23/16.
 */
public class Node implements Comparable<Node>{

  private int x;
  private int y;
  private Terrain terrain;
  private Science science;
  private boolean isOccupied;
  private Node parent;
  private Node destination;
  private int shortestDistance;

  public Node(){

  }

  public Node(int x, int y, Terrain terrain, Science science, boolean isOccupied){
    this.x = x;
    this.y = y;
    this.terrain = terrain;
    this.science = science;
    this.isOccupied = isOccupied;
    this.parent = null;
    this.destination = null;
    this.shortestDistance = Integer.MAX_VALUE;
  }

  public Node(Node copy){
    this.x = copy.getX();
    this.y = copy.getY();
    this.terrain = copy.getTerrain();
    this.science = copy.getScience();
    this.isOccupied = copy.isOccupied();
    this.parent = copy.getParent();
    this.destination = copy.getDestination();
    this.shortestDistance = copy.getShortestDistance();
  }

  public double h(){
    // TODO heuristic
    return 0;
  }

  public double g(){
    // TODO actual distance
    return 0;

  }

  public int f(){
    // TODO sum of g and h
    return 0;
  }

  @Override
  public int compareTo(Node otherNode){
    // FIXME:
    return 0;
  }

  @Override
  public boolean equals(Object other){
    if(this == other) return true;
    if(!(other instanceof Node)) return false;
    Node otherNode = (Node) other;
    return x == otherNode.getX() &&
            y == otherNode.getY() &&
            terrain == otherNode.getTerrain() &&
            science == otherNode.getScience() &&
            isOccupied == otherNode.isOccupied();
  }

  @Override
  public int hashCode(){
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    result = 31 * result + terrain.hashCode();
    result = 31 * result + science.hashCode();
    result = 31 * result + (isOccupied ? 1 : 0);
    return result;
  }

  @Override
  public String toString(){
    return String.format("{x:%d, y:%d, terrain:%s, science:%s, occupied:%b}", x, y, terrain, science, isOccupied);
  }

  public int getX(){
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public void setTerrain(Terrain terrain) {
    this.terrain = terrain;
  }

  public Science getScience() {
    return science;
  }

  public void setScience(Science science) {
    this.science = science;
  }

  public boolean isOccupied() {
    return isOccupied;
  }

  public void setOccupied(boolean occupied) {
    isOccupied = occupied;
  }

  public void prepareForSearch(){
    this.parent = null;
    this.destination = null;
    this.shortestDistance = Integer.MAX_VALUE;
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public Node getDestination() {
    return destination;
  }

  public void setDestination(Node destination) {
    this.destination = destination;
  }

  public int getShortestDistance() {
    return shortestDistance;
  }

  public void setShortestDistance(int shortestDistance) {
    this.shortestDistance = shortestDistance;
  }
}
