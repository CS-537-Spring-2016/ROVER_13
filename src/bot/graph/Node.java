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

  public Node(){

  }

  public Node(int x, int y, Terrain terrain, Science science, boolean isOccupied){
    this.x = x;
    this.y = y;
    this.terrain = terrain;
    this.science = science;
    this.isOccupied = isOccupied;
  }

  public Node(Node copy){
    this.x = copy.getX();
    this.y = copy.getY();
    this.terrain = copy.getTerrain();
    this.science = copy.getScience();
    this.isOccupied = copy.isOccupied();
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

  public int compareTo(Node otherNode){
    return 0;
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
}
