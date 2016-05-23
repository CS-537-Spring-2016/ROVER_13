package bot.location;

import bot.movement.Direction;
import enums.Science;
import enums.Terrain;

import java.util.*;

/**
 * Created by tj on 5/1/16.
 */
public class CellMap {
  private Map<Location, Cell> cellMap;
  // possible optimization to use 1 object for key and just update  x and y

  public CellMap(){
    this.cellMap = new HashMap<>();
  }

  public Cell getCell(int x, int y){
    Location key = new Location(x,y);
    return cellMap.get(key);
  }

  public Cell getCellByCardinality(Direction direction, Cell cell){
    Location key = null;
    if(direction == Direction.NORTH){
      key = new Location(cell.getxPosition(), cell.getyPosition() - 1);
    }
    else if(direction == Direction.SOUTH){
      key = new Location(cell.getxPosition(), cell.getyPosition() + 1);
    }
    else if(direction == Direction.EAST){
      key = new Location(cell.getxPosition() + 1, cell.getyPosition());
    }
    else if(direction == Direction.WEST){
      key = new Location(cell.getxPosition() - 1, cell.getyPosition());
    }
    return cellMap.get(key);
  }

  public Collection<Cell> getCells(){
    return cellMap.values();
  }

  public boolean contains(int x, int y){
    Location key = new Location(x,y);
    return cellMap.containsKey(key);
  }

  // this method will add a new cell if it doesn't exist or overwrite the previous cell
  // if it already exists
  public boolean addCell(Cell cell){
    Location key = new Location(cell.getxPosition(), cell.getyPosition());
    if(cellMap.containsKey(key)){
      updateCell(key, cell);
    } else {
      cellMap.put(key, cell);
    }
    return true;
  }

  public Cell removeCell(int x, int y){
    Location key = new Location(x,y);
    return cellMap.remove(key);
  }

  // precondition: called only if the cell exists
  private Cell updateCell(Location key, Cell cell){
    return cellMap.put(key, cell);
  }

  public List<Cell> getOrganicCells(){
    List<Cell> organics = new ArrayList<>();
    for(Cell cell : cellMap.values()){
      if(cell.getScience().equals(Science.ORGANIC)){
        organics.add(cell);
      }
    }
    return organics;
  }

  public List<Cell> getExcavableScienceCells(){
    List<Cell> science = new ArrayList<>();
    for(Cell cell : cellMap.values()){
      if(!cell.getScience().equals(Science.NONE) &&
              (cell.getTerrain().equals(Terrain.SOIL) ||
                      cell.getTerrain().equals(Terrain.SAND) ||
                      cell.getTerrain().equals(Terrain.GRAVEL) ) ){
        science.add(cell);
      }
    }
    return science;
  }

}
