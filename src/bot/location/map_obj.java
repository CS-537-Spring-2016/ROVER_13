package rover;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

import rover.scanmap;
import com.google.gson.Gson;
public class map_obj {

public List<scanmap> convertor(String jsonObject)
{
	Gson gson = new Gson();
	// 	The scanmap class will have 3 attributes,
	//first will be an two dimensional 7*7 object array of scanArray class,
	//second will be a centerpoint class object
	//third will be edgesize variable.
	//this class defintion matches the format of data passed through json
	//so json should be converted to array of scanmap objects
	scanmap[] scanmap=gson.fromJson(jsonObject, scanmap[].class);
	//following line will convert array of objects into a list and return it.
	ArrayList<scanmap> li=new ArrayList<scanmap>(Arrays.asList(scanmap));
	return li;
	
	
	
}
}
