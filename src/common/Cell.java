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
	Cell(){
		terrain=Terrain.SOIL;
		science = Science.NONE;
		hasRover=false;
	}

}
