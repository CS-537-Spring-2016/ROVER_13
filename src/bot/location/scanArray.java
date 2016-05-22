package rover;
import enums.Terrain;
import enums.Science;
public class scanArray {
private Terrain terrain;
private int elevation;
private Science science;
private boolean hasRover;
public Terrain getTerrain() {
	return terrain;
}
public void setTerrain(Terrain terrain) {
	this.terrain = terrain;
}
public int getElevation() {
	return elevation;
}
public void setElevation(int elevation) {
	this.elevation = elevation;
}
public Science getScience() {
	return science;
}
public void setScience(Science science) {
	this.science = science;
}
public boolean isHasRover() {
	return hasRover;
}
public void setHasRover(boolean hasRover) {
	this.hasRover = hasRover;
}




}
