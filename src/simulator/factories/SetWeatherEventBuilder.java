package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	public SetWeatherEventBuilder(){
		super("set_weather");
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		
		int time = data.getInt("time");
		
		JSONArray jsonArray = data.getJSONArray("info");
		List<JSONObject> info = new ArrayList<JSONObject>();
		for(int i = 0; i < jsonArray.length(); i++)
			info.add(jsonArray.getJSONObject(i));
		
		
		List<Pair<String, Weather>> pairs = new ArrayList<Pair<String, Weather>>();
		for(int i = 0; i < info.size(); i++) {
			String weatherString = info.get(i).getString("weather");
			pairs.add(new Pair<> (info.get(i).getString("road"), Weather.valueOf(weatherString)));
		}
		return new SetWeatherEvent(time, pairs);
	}

}
