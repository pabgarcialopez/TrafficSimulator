package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	public NewVehicleEventBuilder(){
		super("new_vehicle");
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		
		JSONArray jsonArray = data.getJSONArray("itinerary");
		
		List<String> listJunctions = new ArrayList<String>();
		
		for(int i = 0; i < jsonArray.length(); i++) 
			listJunctions.add(jsonArray.getString(i));
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, listJunctions);
	}
}
