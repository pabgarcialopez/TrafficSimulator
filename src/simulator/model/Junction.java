package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private int x;
	private int y;
	private List<Road> inRoads;
	private Map<Junction, Road> outRoads;
	private List<List<Vehicle>> queue;
	private Map <Road, List <Vehicle>> roadQueue;
	private int greenLightIndex; // el indice de la carretera entrante (en la lista de carreteras entrantes) que tiene el semáforo en verde
	private int lastSwitchingTime;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		
		if(lsStrategy == null || dqStrategy == null) 
			throw new IllegalArgumentException("Strategy must not be null.");
		else if(xCoor < 0 || yCoor < 0)
			throw new IllegalArgumentException("Coordinates must be positive.");
		
		this.inRoads = new ArrayList<Road>();
		this.outRoads = new HashMap<Junction, Road>();
		this.queue = new ArrayList<List<Vehicle>>();
		this.roadQueue = new HashMap<Road, List<Vehicle>>();
		
		this.greenLightIndex = -1; // Todas las carreteras con su semaforo en rojo.
		this.lastSwitchingTime = 0;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
	}

	@Override
	void advance(int time) {
		
		// 1
		
		// Si la carretera esta en rojo, no hay que mover coches.
		if(greenLightIndex != -1) {
			// Primero buscamos la lista de vehiculos correspondiente a la carretera con el greenLightIndex
			List<Vehicle> vehicles = queue.get(greenLightIndex);
			List <Vehicle> vehiclesToBeMoved = dqStrategy.dequeue(vehicles);
			
			// Les pedimos que se muevan a la siguiente carretera.
			for(Vehicle v: vehiclesToBeMoved) {
				v.moveToNextRoad();
				vehicles.remove(v);
			}
				
		}
		
		// 2
		int newLightIndex = lsStrategy.chooseNextGreen(inRoads, queue, greenLightIndex, lastSwitchingTime, time);
		
		if(newLightIndex != greenLightIndex) {
			greenLightIndex = newLightIndex;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {
		
		JSONObject json = new JSONObject();
		
		if(greenLightIndex != -1)
			json.put("green", inRoads.get(greenLightIndex).toString());
		else json.put("green", "none");
		
		JSONArray ja = new JSONArray();
		for(int i = 0; i < queue.size(); i++) {
			
			JSONObject aux = new JSONObject(); // Qi
			
			// Cogemos id de carretera de Qi.
			aux.put("road", inRoads.get(i).getId());//queue.get(i).get(0).getRoad().getId());
			
			// Creamos el array de vehiculos de la cola de Qi.
			JSONArray auxVehicleArray = new JSONArray();
			for(int j = 0; j < queue.get(i).size(); j++)
				auxVehicleArray.put(queue.get(i).get(j).getId());
			
			aux.put("vehicles", auxVehicleArray);
			
			// Introducimos este Qi en el JasonArray.
			ja.put(aux);
		}
		
		json.put("queues", ja);
		
		json.put("id", super._id);
		
		return json;
	}
	
	void addIncommingRoad(Road r) {
		
		if (!r.getDest().equals(this))
			throw new IllegalArgumentException("Road is not an incoming road.");

		inRoads.add(r);
		List<Vehicle> newQueue = new LinkedList<Vehicle>();
		queue.add(newQueue);
		roadQueue.put(r, newQueue);

	}
	
	void addOutgoingRoad(Road r) {
		
		// Vemos primero que no exista una carretera que vaya a este cruce, y que la carretera sea saliente del cruce.
		if(outRoads.containsKey(r.getDest()))
			throw new IllegalArgumentException("A road with this junction destination already exists.");
		
		else if(!r.getSrc().equals(this))
			throw new IllegalArgumentException("The road is not an outgoing road.");
		
		outRoads.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		Road road = v.getRoad();
		roadQueue.get(road).add(v);
	}
	
	Road roadTo(Junction j) {
		return outRoads.get(j);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getGreenLightIndex() {
		return greenLightIndex;
	}
	
	public List<Road> getInRoads() {
		return inRoads;
	}
	
	public List<Vehicle> getQueue(Road road) {
		return roadQueue.get(road);
	}
	

}
