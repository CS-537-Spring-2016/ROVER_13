<<<<<<< HEAD
package rover;


=======
package bot.location;

import bot.graph.Node;
>>>>>>> b9dadd157f75b61b5bec232c26d5a58bc9935de9
import enums.Science;
import enums.Terrain;

public class Cell {

<<<<<<< HEAD
	public int xPosition;
	public int yPosition;
	private Terrain terrain;
	private Science science;

	// For occupied status we will use hasRover boolean attribute
	private boolean hasRover;
	
	//constructor for setting x and y position
	public Cell(int x, int y){
		this.xPosition=x;
		this.yPosition=y;
	}

	// default constructor
	public Cell() {
		terrain = Terrain.SOIL;
		science = Science.NONE;
		hasRover = false;
	}

	// Constructor for setting Terrain, science and occupied position
	public Cell(Terrain terrain, Science science, boolean hasRover) {
		this.terrain = terrain;
		this.science = science;
		this.hasRover = hasRover;
	}
	// getters
	// we will not create any setters for security reasons. Attributes can be
	// set using construcors if needed
	public int getxPosition() {
		return xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public Science getScience() {
		return science;
	}

	public boolean isHasRover() {
		return hasRover;
	}


	
=======
  public int xPosition;
  public int yPosition;
  private Terrain terrain;
  private Science science;

  // For occupied status we will use hasRover boolean attribute
  private boolean hasRover;

  //constructor for setting x and y position
  public Cell(int x, int y){
    this.xPosition=x;
    this.yPosition=y;
  }

  // default constructor
  public Cell() {
    terrain = Terrain.SOIL;
    science = Science.NONE;
    hasRover = false;
  }

  // Constructor for setting Terrain, science and occupied position
  public Cell(Terrain terrain, Science science, boolean hasRover) {
    this.terrain = terrain;
    this.science = science;
    this.hasRover = hasRover;
  }
  // getters
  // we will not create any setters for security reasons. Attributes can be
  // set using construcors if needed
  public int getxPosition() {
    return xPosition;
  }

  public int getyPosition() {
    return yPosition;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public Science getScience() {
    return science;
  }

  public boolean isHasRover() {
    return hasRover;
  }

  public Node cellToNode(){
    return new Node(xPosition, yPosition, terrain, science, hasRover);
  }

>>>>>>> b9dadd157f75b61b5bec232c26d5a58bc9935de9

}