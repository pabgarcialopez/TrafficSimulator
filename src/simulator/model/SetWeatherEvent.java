package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{

	List<Pair<String, Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) { 
		super(time);
		if(ws == null)
			throw new IllegalArgumentException("ws list must not be null.");
		this.ws = ws;
		
	}
	
	@Override
	void execute(RoadMap map) {
		for(int i = 0; i < ws.size(); i++) {
			Road r = map.getRoad(ws.get(i).getFirst());
			if(r == null)
					throw new IllegalArgumentException("Road must exit.");
			r.setWeather(ws.get(i).getSecond());
		}
	}
	
	@Override
	public String toString() {
		
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("Change Weather: [");
		
		for(int i = 0; i < ws.size(); i++) {
			buffer.append("(");
			buffer.append(ws.get(i).getFirst());
			buffer.append(",");
			buffer.append(ws.get(i).getSecond().toString());
			buffer.append(")");
			
			// Para la separacion entre varios.
			if(i != ws.size() - 1)
				buffer.append(", ");
		}
		
		buffer.append("]");
		
		return buffer.toString();
	}

}
