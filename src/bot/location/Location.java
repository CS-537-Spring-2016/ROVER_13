package bot.location;




public class Location {

private int x;
private int y;
  public  Location(int x,int y) {
      this.x=x;
      this.y=y;
  }

  public int getX() {
     return x;
  }
  public int getY() {
        return y;
  }

  @Override
  public boolean equals(Object obj){
    if(this == obj){
      return true;
    }
    if(!(obj instanceof Location)){
      return false;
    }
    Location other = (Location) obj;
    return this.x == other.x && this.y == other.y;
  }

<<<<<<< HEAD
  @Override
  public int hashCode(){
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    return result;
  }

=======
>>>>>>> 29449d94c7c4ab428ecda8d2a8ff046cb57a2e77
}
