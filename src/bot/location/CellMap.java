package bot.location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tj on 5/1/16.
 */
public class CellMap {
  private Map<Location, Cell> cellMap;
  // possible optimization to use 1 object for key and just update x and y

  public CellMap(){
    this.cellMap = new HashMap<>();
  }

  public Cell getCell(int x, int y){
    Location key = new Location(x,y);
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
}
