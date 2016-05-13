package bot.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Coord;
import common.MapTile;
import common.ScanMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tj on 5/7/16.
 */
public class CellScanner {

  public List<Cell> convertToCells(String jsonScanMap){
    List<Cell> cells = new ArrayList<>();
    /*
    TODO: implement logic to create valid cells here

     */
    Gson gson = new GsonBuilder().create();
    ScanMap scanMap = gson.fromJson(jsonScanMap, ScanMap.class);
    MapTile[][] tiles = scanMap.getScanMap();
    int edgeSize = scanMap.getEdgeSize();
    int offSet = edgeSize / 2;
    Coord center = scanMap.getCenterPoint();
    int x = center.xpos;
    int y = center.ypos;
    MapTile currentTile;
    Cell currentCell;
    for(int row = 0; row < tiles.length; row++){
      for(int col = 0; col < tiles.length; col++){
        currentTile = tiles[row][col];
        currentCell = new Cell(col - offSet + x, row - offSet + y, currentTile.getTerrain(),
                currentTile.getScience(), currentTile.getHasRover()
        );
        cells.add(currentCell);
      }
    }
    System.out.printf("CENTER POINT: x:%d y:%d%n%n", x, y);
    return cells;
  }
}
