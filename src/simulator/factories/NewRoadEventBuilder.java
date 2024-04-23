package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	int time;
	String id;
	String jsdest;
	String jsrc;
	int length;
	int co2Limit;
	int maxSpeed;
	String stringWeather;
	Weather weather;
	
	NewRoadEventBuilder(String type){
		super(type);
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		time = data.getInt("time");
		id = data.getString("id");
	    jsrc = data.getString("src");
		jsdest = data.getString("dest");
		length = data.getInt("length");
		co2Limit = data.getInt("co2limit");
		maxSpeed = data.getInt("maxspeed");
		stringWeather = data.getString("weather");
		weather = Weather.valueOf(stringWeather);
		return createTheRoad();
	}
	abstract Event createTheRoad();

}
