package bot.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Coord;
import common.MapTile;
import common.ScanMap;
import enums.Science;
import enums.Terrain;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;




/**
 * Created by tj on 5/7/16.
 */
public class CellScanner {

  public List<Cell> convertToCells(String jsonScanMap){
    List<Cell> cells = new ArrayList<>();
    Gson gson = new GsonBuilder().create();
    ScanMap scanMap = gson.fromJson(jsonScanMap, ScanMap.class);
    MapTile[][] tiles = scanMap.getScanMap();
    int edgeSize = scanMap.getEdgeSize();
    int offSet = edgeSize / 2;
    Coord center = scanMap.getCenterPoint();
    int x = center.xpos;
    int y = center.ypos;
    int cellX;
    int cellY;
    MapTile currentTile;
    Cell currentCell;
    for(int col = 0; col < tiles.length; col++){
      for(int row = 0; row < tiles.length; row++){
        cellX = col - offSet + x;
        cellY = row - offSet + y;
        currentTile = tiles[col][row];
        if(cellX >= 0 && cellY >= 0){
          currentCell = new Cell(cellX, cellY, currentTile.getTerrain(),
                  currentTile.getScience(), currentTile.getHasRover()
          );
          cells.add(currentCell);
        }
      }
    }
    return cells;
  }

  public List<Cell> convertServerCells(JSONArray json) {

	  
    
 // To Get : Info from json regarding science and terrain
    List<Cell> cells = new ArrayList<>(json.size());
    Cell currentCell;
    JSONObject jsonObj;
    try {
    for(Object obj : json.toArray()){
      jsonObj = (JSONObject) obj;
      int x = (int)(long)jsonObj.get("x");
      int y = (int)(long)jsonObj.get("y");
	
      Science science =  Science.valueOf(jsonObj.get("science").toString().toUpperCase());
      Terrain terrain = Terrain.valueOf(jsonObj.get("terrain").toString().toUpperCase());
      if(x >= 0 && y >= 0 && !terrain.equals(Terrain.NONE)){
          currentCell = new Cell(x, y, terrain, science, false);
          cells.add(currentCell);
        }
      
    } 
    }
      catch (JSONException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
      
  
   return cells;

  }
}
