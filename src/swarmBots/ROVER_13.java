package swarmBots;

import bot.Rover;
import common.Coord;
import common.MapTile;
import common.ScanMap;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ROVER_13 {
	BufferedReader in;
	PrintWriter out;
	String rovername;
	ScanMap scanMap;
	int sleepTime;
	String SERVER_ADDRESS = "localhost";
	static final int PORT_ADDRESS = 9537;
	public static Map<Coord, MapTile> globalMap;
	List<Coord> destinations;
	long counter;//for controlling the traffic
	
	//default constructor
	public ROVER_13(){
		rovername = "ROVER_13";
		SERVER_ADDRESS = "localhost";
		sleepTime=300;
		globalMap = new HashMap<>();
		destinations = new ArrayList<>();
	}
		
		
	//Constructor with server address 
	public ROVER_13(String serverAdress){
		rovername = "ROVER_13";
		SERVER_ADDRESS = serverAdress;
		sleepTime=300;
		globalMap = new HashMap<>();
		destinations = new ArrayList<>();
	}
	
	
  public static void main(String[] args){
   
  }
}
