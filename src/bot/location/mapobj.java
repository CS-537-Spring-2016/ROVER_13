package bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.*;

public class mapobj
{

	private int xpos;
private int ypos;
private String terrain;
private String science;
private String occu;

public mapobj(int x, int y, String terrain, String sci, String occu) {
	
	super();

	this.xpos = x;
	this.ypos = y;
	this.terrain = terrain;
	this.science = sci;
	this.occu = occu;

}

public mapobj() {
	// TODO Auto-generated constructor stub

}

public List<Cell> convertor(String json)

{

	Gson gson = new Gson();
	Cell m=gson.fromJson(json,Cell.class);//it will convert json object into java object given that the java objects have same attributes
	List<Cell> li=new ArrayList<Cell>();
	li.add(m);
	return li;

}
}