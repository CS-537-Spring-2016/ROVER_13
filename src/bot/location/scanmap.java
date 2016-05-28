package rover;
import rover.centerpoint;
import rover.scanArray;
public class scanmap {
private scanArray scanArray[][];
private centerpoint centerpoint;
private int edgesize;
public scanmap(rover.scanArray[][] scanArray, rover.centerpoint centerpoint, int edgesize) {
	super();
	this.scanArray = scanArray;
	this.centerpoint = centerpoint;
	this.edgesize = edgesize;
}
public centerpoint getCenterpoint() {
	return centerpoint;
}
public void setCenterpoint(centerpoint centerpoint) {
	this.centerpoint = centerpoint;
}
public scanArray[][] getScanArray() {
	return scanArray;
}
public void setScanArray(scanArray[][] scanArray) {
	this.scanArray = scanArray;
}
public int getEdgesize() {
	return edgesize;
}
public void setEdgesize(int edgesize) {
	this.edgesize = edgesize;
}





}
