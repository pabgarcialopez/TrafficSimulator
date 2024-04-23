package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap roadMap;
	private List<Event> events; // Tiene que estar ordenada por el tiempo de los eventos
	private int time;
	private List<TrafficSimObserver> observables;
	
	public TrafficSimulator() {
		this.time = 0;
		this.roadMap = new RoadMap();
		this.events = new SortedArrayList<Event>();
		this.observables = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		
		if(e.getTime() <= time) {
			String message = "The event's time (" + e.getTime() + ") must be larger than the current time (" + time + ")";
			
			notifyError(message);
			throw new IllegalArgumentException(message);
		}
		
		events.add(e); // Se añade ordenado directamente
		
		notifyEventAdded(e);
	}

	public void advance() {
		// Incrementamos el tiempo de la simulacion.
		time++;

		// Notificamos el comienzo del advance a la vista
		notifyAdvanceStart();
		
		// Ejecutamos todos los eventos asociados a este tiempo y los eliminamos de la lista
		List<Junction> junctions = roadMap.getJunctions();
		List<Road> roads = roadMap.getRoads();
		
		try {
			
			while(events.size() > 0 && events.get(0).getTime() == time)
				events.remove(0).execute(roadMap);
			
			// Llamamos al advance de todos los cruces
			for(int i = 0; i < junctions.size(); i++)
				junctions.get(i).advance(time);
		
			// Llamamos al advance de todas las carreteras.
			for(int i = 0; i < roads.size(); i++)
				roads.get(i).advance(time);
		}
		
		catch(IllegalArgumentException iae) {
			notifyError(iae.getMessage()); //esto son errores de ejecucion
			throw iae;
		}
		
		// Notificamos el fin del advance a la vista
		notifyAdvanceEnd();
	}

	public void reset() {
		roadMap.reset();
		events.clear();
		time = 0;
		
		notifyReset();
	}
	
	@Override
	public void addObserver(TrafficSimObserver observer) {
		notifyRegister(observer);
		observables.add(observer);
		
	}

	@Override
	public void removeObserver(TrafficSimObserver observer) {
		observables.remove(observer);
	}
	
	private void notifyAdvanceStart() {
		for(TrafficSimObserver obs: observables) {
			obs.onAdvanceStart(roadMap, events, time);
		}
	}
	
	private void notifyAdvanceEnd() {
		for(TrafficSimObserver obs: observables) {
			obs.onAdvanceEnd(roadMap, events, time);
		}
	}
	
	private void notifyEventAdded(Event event) {
		for(TrafficSimObserver obs: observables) {
			obs.onEventAdded(roadMap, events, event, time);
		}
	}
	
	private void notifyReset() {
		for(TrafficSimObserver obs: observables) {
			obs.onReset(roadMap, events, time);
		}
	}
	
	private void notifyRegister(TrafficSimObserver o) {
		o.onRegister(roadMap, events, time);
	}
	
	private void notifyError(String err) {
		for(TrafficSimObserver obs: observables) {
			obs.onError(err);
		}
	}
	
	public JSONObject report() {
		
		JSONObject json = new JSONObject();
		json.put("time", time);
		json.put("state", roadMap.report());
		
		return json;
	}
}
