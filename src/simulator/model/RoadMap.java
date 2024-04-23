package simulator.model;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RoadMap {
	
	private List <Junction> junctionList;
	private List <Road> roadList;
	private List <Vehicle> vehicleList;
	private Map <String, Junction> junctionMap;
	private Map <String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;
	
	RoadMap() {
		junctionList = new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		vehicleList = new ArrayList<Vehicle>();
		junctionMap = new HashMap<String, Junction>();
		roadMap = new HashMap<String, Road>();
		vehicleMap = new HashMap<String, Vehicle>();
	}
	
	void addJunction(Junction j) {
		
		// Comprobamos que no exista un cruce con ese identificador.
		if(junctionMap.containsKey(j.getId()))
			throw new IllegalArgumentException("A junction with that identifier already exists.");
		
		junctionList.add(j);
		junctionMap.put(j.getId(), j);
	}
	
	void addRoad(Road r) {
		
		// Comprobamos que no exista ya una carretera con ese identificador.
		if(roadMap.containsKey(r.getId()))
			throw new IllegalArgumentException("A road with that identifier already exists.");
		
		// Comprobamos que los cruces que conectan la carretera existen en la lista de cruces.
		if(!junctionMap.containsKey(r.getSrc().getId()) || !junctionMap.containsKey(r.getDest().getId()))
			throw new IllegalArgumentException("Source and destination junctions must exist in junctionList");
		
		roadList.add(r);
		roadMap.put(r.getId(), r);
	}
	
	void addVehicle(Vehicle v) {
		
		// Comprobamos que no exista un vehiculo con ese identificador.
		if(vehicleMap.containsKey(v.getId())) 
			throw new IllegalArgumentException("A vehicle with that identifier already exists.");
		
		// Comprobamos que el itinerario es valido (para cada par de cruces consecutivos, existe una carretera que los conecta).
		List <Junction> itinerary = v.getItinerary();
		for(int i = 0; i < itinerary.size()-1; i++) {
			if(itinerary.get(i).roadTo(itinerary.get(i+1)) == null)
				throw new IllegalArgumentException("Itinerary must be valid.");
		}
		
		vehicleList.add(v);
		vehicleMap.put(v.getId(), v);
	}
	
	public Junction getJunction(String id) {
		return junctionMap.get(id);
	}
	
	public Road getRoad(String id) {
		return roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehicleMap.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionList);
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadList);
	}
	
	public List<Vehicle>getVehicles(){
		return Collections.unmodifiableList(vehicleList);
	}
	
	void reset() {
		junctionList.clear();
		roadList.clear();
		vehicleList.clear();
		junctionMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}
	
	public JSONObject report() {
		JSONObject json = new JSONObject();
		JSONArray j1 = new JSONArray(); // Para los roads
		JSONArray j2 = new JSONArray(); // Para los vehicles
		JSONArray j3 = new JSONArray(); // Para los junctions
		
		for(int i = 0; i < roadList.size(); i++)
			j1.put(roadList.get(i).report());
		json.put("roads", j1);
		
		
		for(int i = 0; i < vehicleList.size(); i++)
			j2.put(vehicleList.get(i).report());
		json.put("vehicles", j2);
		
		
		for(int i = 0; i < junctionList.size(); i++)
			j3.put(junctionList.get(i).report());
		json.put("junctions", j3);
		
		return json;
	}
	
 
}
