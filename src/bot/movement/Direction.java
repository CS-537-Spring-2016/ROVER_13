package bot.movement;

public enum Direction {
  NORTH("N"),
  EAST("E"),
  WEST("W"),
  SOUTH("S");

  private final String abbrev;

  Direction(String abbrev){
    this.abbrev = abbrev;
  }

  public String getAbbrev(){
    return this.abbrev;
  }
}
