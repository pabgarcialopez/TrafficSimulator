package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator trafficSimulator;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory <Event> eventsFactory){
		
		if(sim == null || eventsFactory == null)
			throw new IllegalArgumentException("Some argument in Controller constructor is null.");
		
		this.trafficSimulator = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void reset() {
		trafficSimulator.reset();
	}
	
	public void loadEvents(InputStream in) {
		
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray jsonArray = jo.getJSONArray("events");
		
		for(int i = 0; i < jsonArray.length(); i++) {
			JSONObject ei = jsonArray.getJSONObject(i);
			trafficSimulator.addEvent(eventsFactory.createInstance(ei));
		}
	}
	
	public void run(int n) {
		run(n, null);
	}
	
	public void run (int n, OutputStream output) {
	
		int i = 0;
		
		PrintStream out = null;
		
		if(output != null) {
			out = new PrintStream(output);
			
			out.println("{");
			out.println("  \"states\": [");
		}
		

		while(i < n) {
			
			trafficSimulator.advance();
			
			if(output != null) {
				
				out.print(trafficSimulator.report());
				
				if(i != n - 1)
					out.println(",");
				
				else out.println();
			}
				
			
			i++;
		}
		
		if(output != null)
			out.println("]\n}");
			
		// hacemos un while con el numero de ticks, y llamamos al advanced
		// luego impirmos los estados, llamando al report del TrafficSimulator que llama al report de RoadMap.
		// lo más facil es crear un print Stream (Página 96 de las diapositivas).
	}
	
	
	
	public void addObserver(TrafficSimObserver o) {
		trafficSimulator.addObserver(o);
	}
	public void removeObserver(TrafficSimObserver o) {
		trafficSimulator.removeObserver(o);
	}
	public void addEvent(Event e) {
		trafficSimulator.addEvent(e);
	}
}
