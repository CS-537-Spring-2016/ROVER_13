package swarmBots;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.Communication;
import common.Coord;
import common.MapTile;
import common.ScanMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.RoverDriveType;
import enums.Science;
import enums.Terrain;
import bot.SearchLogic;
import supportTools.CommunicationHelper;

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
	int counter = 0;

	// defailt constructor
	public ROVER_13() {
		rovername = "ROVER_13";
		System.out.println(rovername + " rover object constructed");
		SERVER_ADDRESS = "localhost";
		sleepTime = 300; // in milliseconds - smaller is faster, but the server
							// will cut connection if it is too small
		globalMap = new HashMap<>();
		destinations = new ArrayList<>();
	}

	/*
	 * Constructor with serverAddress Parameter as commandline argument
	 */
	public ROVER_13(String serverAddress) {
		System.out.println(rovername + " rover object constructed");
		rovername = "ROVER_13";
		SERVER_ADDRESS = serverAddress;
		sleepTime = 300; // in milliseconds - smaller is faster, but the server
							// will cut connection if it is too small
		globalMap = new HashMap<>();
		destinations = new ArrayList<>();
	}

	/*
	 * Connects to the server then enters the processing loop.
	 */
	private void run() throws IOException, InterruptedException {

		// Make connection and initialize streams
		Socket socket = new Socket(SERVER_ADDRESS, PORT_ADDRESS); // set port
																	// here
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Process all messages from server, wait until server requests Rover ID
		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(rovername); // This sets the name of this instance
				// of a swarmBot for identifying the
				// thread to the server
				break;
			}
		}
		// -------------------------------------------------------------Rover
		// logic -------------------------------------------------------------
		String line = "";
		boolean stuck = false; // just means it did not change locations between
								// requests,
		// boolean blocked = false;
		String[] cardinals = new String[4];
		cardinals[0] = "N";
		cardinals[1] = "E";
		cardinals[2] = "S";
		cardinals[3] = "W";
		Coord currentLoc = null;
		Coord previousLoc = null;
		Coord rovergroupStartPosition = null;
		Coord targetLocation = null;

		out.println("START_LOC"); // gets the current position of the rover

		line = in.readLine();
		if (line == null) {
			System.out.println(rovername + " check connection to server");
			line = "";
		}

		if (line.startsWith("START_LOC")) {
			rovergroupStartPosition = extractLocation(line);
		}
		System.out.println(rovername + " START_LOC " + rovergroupStartPosition);

		// **** Request TARGET_LOC Location from SwarmServer ****
		out.println("TARGET_LOC");
		line = in.readLine();
		if (line == null) {
			System.out.println(rovername + " check connection to server");
			line = "";
		}
		if (line.startsWith("TARGET_LOC")) {
			targetLocation = extractLocation(line);
		}
		System.out.println(rovername + " TARGET_LOC " + targetLocation);

		// ******* destination *******

		SearchLogic search = new SearchLogic();

		// ******** define Communication
		String url = "http://23.251.155.186:3000/api";
		String corp_secret = "0FSj7Pn23t";
		Communication com = new Communication(url, rovername, corp_secret);

		// Get destinations from Sensor group.
		List<Coord> blockedDestinations = new ArrayList<>();
		destinations.add(targetLocation); // adding destinations to the
											// arraylist

		Coord destination = null;

		// start Rover controller process
		while (true) {

			// currently the requirements allow sensor calls to be made with no
			// simulated resource cost

			// **** location call ****
			out.println("LOC");// current location
			line = in.readLine();
			if (line == null) {
				System.out.println(rovername + " check connection to server");
				line = "";
			}
			if (line.startsWith("LOC")) {
				// loc = line.substring(4);
				currentLoc = extractLocation(line);
			}
			System.out.println(rovername + " currentLoc at start: " + currentLoc);

			// after getting location set previous equal current to be able to
			// check for stuckness and blocked later
			previousLoc = currentLoc;

			// **** get equipment listing ****
			ArrayList<String> equipment = new ArrayList<String>();
			equipment = getEquipment();
			// System.out.println("ROVER_13 equipment list results drive " +
			// equipment.get(0));
			System.out.println(rovername + " equipment list results " + equipment + "\n");

			// ***** do a SCAN *****
			// System.out.println("ROVER_13 sending SCAN request");
			this.doScan();
			scanMap.debugPrintMap();

			// upon scan, update my field map
			MapTile[][] scanMapTiles = scanMap.getScanMap();
			updateglobalMap(currentLoc, scanMapTiles);// updating global map

			// ***** communicating with the server
			// System.out.println("post message: " +
			// com.postScanMapTiles(currentLoc, scanMapTiles));
			if (counter == 20) {
				counter = 0;
				/*----------------------------------------Getting global map from communication---------------------------------------*/

				updateglobalMap(com.getGlobalMap()); // getting global map from
														// the communication

				// ********* get closest destination from current location
			
				if (!destinations.isEmpty()) {
					destination = getClosestDestination(currentLoc);
				}

			}
			counter++;
			
			
			// ---------------------------------------------------------- MOVING-----------------------------------------------------------------

			if (search.targetVisible(currentLoc, targetLocation))
				out.println("GATHER");

			//wait for destination from other sensors
			if (destination == null) {

				if (!destinations.isEmpty()) {
					destination = getClosestDestination(currentLoc);
				}

			} else {
				List<String> moves = search.Astar(currentLoc, destination, scanMapTiles, RoverDriveType.TREADS,
						globalMap);
				System.out.println(rovername + "currentLoc: " + currentLoc + ", destination: " + destination);
				System.out.println(rovername + " moves: " + moves.toString());

				// if STILL MOVING
				if (!moves.isEmpty()) {
					out.println("MOVE " + moves.get(0));

					
					if (search.getDistance(currentLoc, destination) < 301) {
						System.out.println("Target visible.");

						// if destination is walkable
						if (search.validateTile(globalMap.get(destination), RoverDriveType.TREADS)) {
							System.out.println("Target Reachable");
						} else {

							blockedDestinations.add(destination);

							// move to new destination
							destinations.remove(destination);
							destination = getClosestDestination(currentLoc);
							System.out.println("Target blocked. Switch target to: " + destination);
						}

					}

					
				} else {
					// check if rover is at the destination, drill
					if (currentLoc.equals(destination)) {
						out.println("GATHER");
						System.out.println(rovername + " arrived destination. Now gathering.");
						if (!destinations.isEmpty()) {
							// remove from destinations
							destinations.remove(destination);
							destination = getClosestDestination(currentLoc);
							System.out.println(rovername + " going to next destination at: " + destination);
						} else {
							System.out.println("Nowhere else to go. Relax..");
						}

					} else {

					}
				}
			}

			System.out.println("destinations: " + destinations);

			// another call for current location
			out.println("LOC");
			line = in.readLine();
			if (line == null) {
				System.out.println(rovername + "ROVER_13 check connection to server");
				line = "";
			}
			if (line.startsWith("LOC")) {
				currentLoc = extractLocation(line);
			}
			stuck = currentLoc.equals(previousLoc);
			Thread.sleep(sleepTime);

			System.out.println(rovername + " ------------ bottom process control --------------");
		}
	}

	private Coord getClosestDestination(Coord currentLoc) {
		double max = Double.MAX_VALUE;
		Coord target = null;

		for (Coord desitnation : destinations) {
			double distance = SearchLogic.getDistance(currentLoc, desitnation);
			if (distance < max) {
				max = distance;
				target = desitnation;
			}
		}
		return target;
	}

	private void updateglobalMap(Coord currentLoc, MapTile[][] scanMapTiles) {
		int centerIndex = (scanMap.getEdgeSize() - 1) / 2;

		for (int row = 0; row < scanMapTiles.length; row++) {
			for (int col = 0; col < scanMapTiles[row].length; col++) {

				MapTile mapTile = scanMapTiles[col][row];

				int xp = currentLoc.xpos - centerIndex + col;
				int yp = currentLoc.ypos - centerIndex + row;
				Coord coord = new Coord(xp, yp);
				globalMap.put(coord, mapTile);
			}
		}
		// put my current position so it is walkable
		MapTile currentMapTile = scanMapTiles[centerIndex][centerIndex].getCopyOfMapTile();
		currentMapTile.setHasRoverFalse();
		globalMap.put(currentLoc, currentMapTile);
	}

	// get data from server and update field map
	private void updateglobalMap(JSONArray data) {

		for (Object o : data) {

			JSONObject jsonObj = (JSONObject) o;
			int x = (int) (long) jsonObj.get("x");
			int y = (int) (long) jsonObj.get("y");
			Coord coord = new Coord(x, y);

			// only bother to save if our globalMap doesn't contain the
			// coordinate
			if (!globalMap.containsKey(coord)) {
				MapTile tile = CommunicationHelper.convertToMapTile(jsonObj);

				// if tile has science AND is not in sand
				if (tile.getScience() != Science.NONE && tile.getTerrain() != Terrain.SAND) {

					// then add to the destination
					if (!destinations.contains(coord))
						destinations.add(coord); // adding the destinations
				}

				globalMap.put(coord, tile);
			}
		}
	}

	/*---------------------------------------------Support Methods ----------------------------------------*/

	private void clearReadLineBuffer() throws IOException {
		while (in.ready()) {
			// System.out.println("ROVER_13 clearing readLine()");
			String garbage = in.readLine();
		}
	}

	// method to retrieve a list of the rover's equipment from the server
	private ArrayList<String> getEquipment() throws IOException {
		// System.out.println("ROVER_13 method getEquipment()");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		out.println("EQUIPMENT");// get the equipment
		String jsonEquipmentListInString = in.readLine(); // grabs the string
															// that was returned
															// first
		if (jsonEquipmentListInString == null) {
			jsonEquipmentListInString = "";
		}
		StringBuilder equipmentListStringBuilder = new StringBuilder(); // StringBuilder
																		// is
																		// mutable
																		// sequence
		if (jsonEquipmentListInString.startsWith("EQUIPMENT")) {
			while (!(jsonEquipmentListInString = in.readLine()).equals("EQUIPMENT_END")) {
				if (jsonEquipmentListInString == null) {
					break;
				}
				// System.out.println("ROVER_13 incomming EQUIPMENT result: " +
				// jsonEqListIn);
				equipmentListStringBuilder.append(jsonEquipmentListInString);
				equipmentListStringBuilder.append("\n");
				// System.out.println("ROVER_13 doScan() bottom of while");
			}
		} else {
			// in case the server call gives unexpected results
			clearReadLineBuffer();
			return null; // server response did not start with "EQUIPMENT"
		}

		String jsonEqListString = equipmentListStringBuilder.toString();
		ArrayList<String> returnList;
		returnList = gson.fromJson(jsonEqListString, new TypeToken<ArrayList<String>>() {
		}.getType());
		return returnList;
	}

	// sends a SCAN request to the server and puts the result in the scanMap
	// array
	public void doScan() throws IOException {
		// System.out.println("ROVER_13 method doScan()");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		out.println("SCAN");

		String jsonScanMapIn = in.readLine(); // grabs the string that was
												// returned first
		if (jsonScanMapIn == null) {
			System.out.println(rovername + " check connection to server");
			jsonScanMapIn = "";
		}
		StringBuilder jsonScanMap = new StringBuilder();
		System.out.println(rovername + " incomming SCAN result - first readline: " + jsonScanMapIn);

		if (jsonScanMapIn.startsWith("SCAN")) {
			while (!(jsonScanMapIn = in.readLine()).equals("SCAN_END")) {
				// System.out.println("ROVER_13 incomming SCAN result: " +
				// jsonScanMapIn);
				jsonScanMap.append(jsonScanMapIn);
				jsonScanMap.append("\n");
				// System.out.println("ROVER_13 doScan() bottom of while");
			}
		} else {
			// in case the server call gives unexpected results
			clearReadLineBuffer();
			return; // server response did not start with "SCAN"
		}
		// System.out.println("ROVER_13 finished scan while");

		String jsonScanMapString = jsonScanMap.toString();
		// debug print json object to a file
		// new MyWriter( jsonScanMapString, 0); //gives a strange result -
		// prints the \n instead of newline character in the file

		// System.out.println("ROVER_13 convert from json back to ScanMap
		// class");
		// convert from the json string back to a ScanMap object
		scanMap = gson.fromJson(jsonScanMapString, ScanMap.class);
	}

	// this takes the server response string, parses out the x and x values and
	// returns a Coord object
	public static Coord extractLocation(String sStr) {
		int indexOf;
		indexOf = sStr.indexOf(" ");
		sStr = sStr.substring(indexOf + 1);
		if (sStr.lastIndexOf(" ") != -1) {
			String xStr = sStr.substring(0, sStr.lastIndexOf(" "));
			System.out.println("extracted xStr " + xStr);

			String yStr = sStr.substring(sStr.lastIndexOf(" ") + 1);
			System.out.println("extracted yStr " + yStr);
			return new Coord(Integer.parseInt(xStr), Integer.parseInt(yStr));
		}
		return null;
	}

	/*
	 * Runs the client
	 */
	public static void main(String[] args) throws Exception {
		ROVER_13 client;
		if (args.length > 0)
			client = new ROVER_13(args[0]);
		else {
			client = new ROVER_13();
		}
		client.run();
	}

}