package swarmBots;

import bot.Rover;
import common.Coord;
import common.MapTile;
import common.ScanMap;

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	long counter;// for controlling the traffic

	// default constructor
	public ROVER_13() {
		rovername = "ROVER_13";
		SERVER_ADDRESS = "localhost";
		sleepTime = 300;
		globalMap = new HashMap<>();
		destinations = new ArrayList<>();
	}

	// Constructor with server address
	public ROVER_13(String serverAdress) {
		rovername = "ROVER_13";
		SERVER_ADDRESS = serverAdress;
		sleepTime = 300;
		globalMap = new HashMap<>();
		destinations = new ArrayList<>();
	}

	/*
	 * Connects to the server
	 */
	private void run() throws IOException, InterruptedException {
		// Make connection
		Socket socket = new Socket(SERVER_ADDRESS, PORT_ADDRESS);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());

		// process all messages from server, wait until server requests roverID

		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(rovername);
				break;
			}
		}

		/*----------------------Rover Logic for Rover 13-----------------------------*/
		String line = "";
		boolean stuck = false;
		boolean blocked = false;

		String[] cardinals = new String[4];
		cardinals[0] = "N";// for north
		cardinals[0] = "E";
		cardinals[0] = "S";
		cardinals[0] = "W";

		String currentDirection = cardinals[0];

		Coord currentLoc = null;
		Coord previousLoc = null;

		Coord roverGroupStartPosition = null;
		Coord targetLocation = null;

		/*-------------------------------------Target Location from Swarm server-------------------*/
		out.println("TARGET_LOC");
		line = in.readLine();
		if (line == null) {
			System.out.println(rovername + " check connection to the server");
			line = "";
		}
		if (line.startsWith("TARGET_LOC")) {
			targetLocation = extractLocation(line);
		}
	}

	/*
	 ********************************************** Supporting Methods*************************************************
	 */
	// takes String value from server response and parse out x and y values
	private Coord extractLocation(String line) {
		// TODO Auto-generated method stub
		int indexOf;
		indexOf = line.indexOf(" ");
		line = line.substring(indexOf + 1);
		if (line.lastIndexOf(" ") != -1) {
			String xString = line.substring(0, line.lastIndexOf(" "));

			String yString = line.substring(line.lastIndexOf(" ") + 1);
			return new Coord(Integer.parseInt(xString), Integer.parseInt(yString));
		}

		return null;
	}

	/*
	 * Runs the client
	 */
	public static void main(String[] args) throws Exception {
		ROVER_13 client;
		// for sending serveraddress in commandline arguments
		if (args.length > 0) {
			client = new ROVER_13(args[0]);
		} else
			client = new ROVER_13();
		client.run();
	}
}
