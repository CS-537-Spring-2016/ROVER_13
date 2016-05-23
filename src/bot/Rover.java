package bot;

import bot.graph.Graph;
import bot.graph.Node;
import bot.graph.search.AStar;
import bot.location.Cell;
import bot.location.CellMap;
import bot.location.CellScanner;
import bot.location.Location;
import bot.movement.*;
import bot.schedule.Scheduler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Communication;
import common.Coord;
import common.MapTile;
import common.ScanMap;
import enums.Science;
import enums.Terrain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rover {

  // communication related
  public static final Logger logger = Logger.getLogger(Rover.class.getName());
  private String roverName;
  private String SERVER_ADDRESS;
  private int SERVER_PORT;
  private long stepCount;
  private CellMap cellMap;
  private Set<Location> visited;
  private Graph graph;
  private Location currentLocation;
  private Scheduler scheduler;
  private String url;
  private Communication communication;
  private BufferedReader in;
  private PrintWriter out;

  // knowledge
  private CollectScienceStrategy collectStrategy;
  private Strategy strategy;
  private String corp_secret = "0FSj7Pn23t";
  public Rover() {
    this("localhost");
  }

  public Rover(String serverAddress) {
    roverName = "ROVER_13";
    SERVER_ADDRESS = serverAddress;
    SERVER_PORT = 9537;
    // FIXME: implement class to cover the sleep time
    strategy = new ExploreStrategy();
    collectStrategy = new CollectScienceStrategy();
    cellMap = new CellMap();
    visited = new HashSet<>();
    graph = new Graph();//testGraph();
    scheduler = new Scheduler();
    url = "http://23.251.155.186:3000/api";
  }

  public void run(){

    try{
      logger.info("connecting to: " + SERVER_ADDRESS);
      Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
    } catch(UnknownHostException unHostEx){
      System.err.printf("unable to connect to socket %s%n", unHostEx);
      unHostEx.printStackTrace();
      return;
    } catch(IOException ioEx){
      System.err.printf("IO exception while creating socket %s%n", ioEx);
      return;
    }

    try{
      serverHandshake();
    } catch(IOException ex){
      System.err.printf("unable to establish handshake with server: %s%n", ex);
      return;
    }

    // rover is alive and communication
    logger.log(Level.INFO, "rover_13 is up");
    communication = new Communication(url, "ROVER_13",corp_secret);
    String lastResponse;
    while(true){
      // TODO do interesting rover things
      requestLocation();
      lastResponse = scan();
      messageServer(lastResponse);
      getUpdateFromServer();
      gather();// because, why not?
      /*
      logic for MOVE, GATHER
      */
      waitUntilReady();
      makeBestMove();
      scheduler.logLastMove();
      stepCount++;
      //
    }

  }

  private void waitUntilReady(){
    scheduler.scheduleNextMoveReady();
//    try{
//      Thread.sleep(sleepTime);
//    } catch(InterruptedException ex){
//      ex.printStackTrace();
//    }
  }

  private void makeBestMove(){
    // decide what direction to move in
//    List<Cell> organics = cellMap.getOrganicCells();
    List<Cell> science = cellMap.getExcavableScienceCells();
    Cell organic = closestOrganic(science);
    Direction direction;
    if(organic != null){
//      direction = collectStrategy.bestMove(graph,
//        new Node(currentLocation.getX(), currentLocation.getY()),
//        organic.cellToNode());
      direction = collectStrategy.bestMove(graph,
              new Node(currentLocation.getX(), currentLocation.getY()),
              organic.cellToNode(),
              visited);
    } else {
      direction = strategy.bestMove(graph, new Node(currentLocation.getX(), currentLocation.getY()), visited);
    }
    move(direction);
  }

  private Cell closestOrganic(List<Cell> organics){
    // FIXME: how to deal when we get an empty list?
    AStar aStar = new AStar();
    Node current = new Node(currentLocation.getX(), currentLocation.getY());
    int minDistance = Integer.MAX_VALUE;
    Cell closest = null;
    for(Cell organic : organics){
      int distance = aStar.minDistance(graph, current, organic.cellToNode());
      if(distance < minDistance){
        closest = organic;
        minDistance = distance;
      }
    }
    return closest;
  }

  private void move(Direction direction){
    logger.info("moving direction: " + direction);
     out.println("MOVE " + direction.getAbbrev());
  }

  private boolean gather(){
    logger.info("attempting to gather");
    out.println("GATHER");
    // FIXME we can perform a check by issuing an equipment call to determine success
    // default to false for now
    return false;
  }


  private String requestLocation(){
    out.println("LOC");
    String response = "";
    try{
      response = in.readLine();
    } catch(IOException ex){
      logger.severe("unable to obtain location from server");
      ex.printStackTrace();
    }
    currentLocation = updateLocation(response);
    return response;
  }

  private Location updateLocation(String response){
    String[] tokens = response.split(" ");
    int x = Integer.parseInt(tokens[1]);
    int y = Integer.parseInt(tokens[2]);
    Location loc = new Location(x,y);
    visited.add(loc);
    return loc;
  }

  private String scan(){
    // TODO
    out.println("SCAN");
    String response = "";
    try{
      String scanStart = in.readLine();
      if(scanStart.equals("SCAN")){
        StringBuilder sb = new StringBuilder();
        String nextLine = in.readLine();
        while(!nextLine.equals("SCAN_END")){
          sb.append(nextLine);
          nextLine = in.readLine();
        }
        response = sb.toString();
        // TODO
        // send response to map builder

        CellScanner cellScanner = new CellScanner();
        List<Cell> cells = cellScanner.convertToCells(response);
        addToCellMap(cells);
        updateGraph(cellMap);
      }
      // parse the response into an object
      // use the object to build a new map/graph
    } catch(IOException ex){
      System.err.println("exception reading outputstream during scan " + ex);
    }
    return response;
  }

  private void serverHandshake() throws IOException{
    String line;
    while(true){
      line = in.readLine();
      if (line.startsWith("SUBMITNAME")) {
        out.println(roverName); // This sets the name of this instance of a swarmBot
        return;
      }
    }
  }

  public void addToCellMap(Collection<Cell> cells){
    for(Cell cell : cells){
      if(cell.getxPosition() >= 0 && cell.getyPosition() >= 0){
        cellMap.addCell(cell);
      }
    }
  }

  public void updateGraph(CellMap cellMap){
    // cells to skip: Terrain.ROCK, Terrain.NONE
    graph = new Graph();
    for(Cell cell : cellMap.getCells()){
      Node currentNode = cell.cellToNode();
      if(isPassable(cell)){
        Cell north = cellMap.getCellByCardinality(Direction.NORTH, cell);
        Cell south = cellMap.getCellByCardinality(Direction.SOUTH, cell);
        Cell east = cellMap.getCellByCardinality(Direction.EAST, cell);
        Cell west = cellMap.getCellByCardinality(Direction.WEST, cell);
        List<Cell> cellList = Arrays.asList(north, south, east, west);
        for(Cell adjCell : cellList){
          if(adjCell != null && isPassable(adjCell)){
            graph.addTwoWayEdge(currentNode, adjCell.cellToNode());
          }
        }
      }
    }
  }

  private boolean isPassable(Cell cell){
    // HARDCODED for our bot
    //TODO: let this logic depend on the type of rover
    return cell.getTerrain() != Terrain.NONE && cell.getTerrain() != Terrain.ROCK;
  }

  private Graph testGraph(){
    Graph graph = new Graph();

    Node n17 = new Node(1,7, Terrain.SOIL, Science.NONE, false);
    Node n18 = new Node(1,8, Terrain.SOIL, Science.NONE, false);
    Node n27 = new Node(2,7, Terrain.SOIL, Science.NONE, false);
    Node n28 = new Node(2,8, Terrain.SOIL, Science.NONE, false);
    Node n37 = new Node(3,7, Terrain.SOIL, Science.NONE, false);
    Node n38 = new Node(3,8, Terrain.SOIL, Science.NONE, false);
    Node n19 = new Node(1,9, Terrain.SOIL, Science.NONE, false);
    Node n29 = new Node(2,9, Terrain.SOIL, Science.NONE, false);
    Node n39 = new Node(3,9, Terrain.SOIL, Science.NONE, false);

    graph.addTwoWayEdge(n17, n18);
    graph.addTwoWayEdge(n18, n19);
    graph.addTwoWayEdge(n17, n27);
    graph.addTwoWayEdge(n27, n28);
    graph.addTwoWayEdge(n28, n29);
    graph.addTwoWayEdge(n18, n28);
    graph.addTwoWayEdge(n19, n29);
    graph.addTwoWayEdge(n27, n37);
    graph.addTwoWayEdge(n37, n38);
    graph.addTwoWayEdge(n28, n38);
    graph.addTwoWayEdge(n38, n39);
    graph.addTwoWayEdge(n29, n39);
    return graph;
  }

  private void messageServer(String response){
    Gson gson = new GsonBuilder().create();
    ScanMap scanMap = gson.fromJson(response, ScanMap.class);
    MapTile[][] tiles = scanMap.getScanMap();
    communication.postScanMapTiles(scanMap.getCenterPoint(), tiles);
  }

  private void getUpdateFromServer(){
    if(stepCount % 5 == 0){
      logger.info("requesting global map");
      JSONArray response = communication.getGlobalMap();
      CellScanner cellScanner = new CellScanner();
      Collection<Cell> cells = cellResponse(response);
      logger.info("global map returned " + cells.size() + " cells");
      addToCellMap(cells);
      updateGraph(cellMap);
    }

  }

  private Collection<Cell> cellResponse(JSONArray response){
    List<Cell> cells = new ArrayList<>(response.size());
    Cell currentCell;
    JSONObject jsonObj;
    for(Object obj : response.toArray()){
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
    return cells;
  }

}
