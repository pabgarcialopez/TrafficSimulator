package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;
public class SetContClassEventBuilder extends Builder <Event>{

	public SetContClassEventBuilder(){
		super("set_cont_class");
	}
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		
		JSONArray jsonArray = data.getJSONArray("info");
		List<JSONObject> info = new ArrayList<JSONObject>();
		for(int i = 0; i < jsonArray.length(); i++) 
			info.add(jsonArray.getJSONObject(i));
		
		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>();
		for(int i = 0; i < info.size(); i++) {
			int contClass = info.get(i).getInt("class");
			String vehicle = info.get(i).getString("vehicle");
			Pair <String, Integer> p = new Pair<> (vehicle, contClass);
			pairs.add(p); //¿como se añade un par?
		}
		
		return new SetContClassEvent(time, pairs);
	}

}
