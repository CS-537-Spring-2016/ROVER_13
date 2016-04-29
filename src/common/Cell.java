package common;

import enums.Science;
import enums.Terrain;

public class Cell {
	
	public int xPosition;
	public int yPosition;
	private Terrain terrain;
	private Science science;
	
	//For occupied status we will use hasRover boolean attribute
	private boolean hasRover;
	
	//default constructor
	public Cell(){
		terrain=Terrain.SOIL;
		science = Science.NONE;
		hasRover=false;
	}
	
	//Constructor for setting Terrain, science and occupied position
	public Cell(Terrain terrain, Science science, boolean hasRover ){
		this.terrain = terrain;
		this.science=science;
		this.hasRover=hasRover;
	}

}
