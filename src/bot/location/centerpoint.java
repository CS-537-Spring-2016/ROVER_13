package rover;

public class centerpoint {
private int xpos;
private int ypos;
public int getXpos() {
	return xpos;
}
public void setXpos(int x) {
	if(x>=0)
	{
	this.xpos = x;
}
	else{
		System.out.println("x coordinate is negative");
	}
}
public int getYpos() {
	return ypos;
}
public void setYpos(int y) {
	if(y>=0)
	{
	this.ypos = y;
}
	else{
		System.out.println("Y coordinate is negative");
	}
}




}
