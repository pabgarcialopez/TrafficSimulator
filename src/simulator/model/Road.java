package simulator.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject{

	private Junction sourceJunction;
	private Junction destinationJunction;
	private int length;
	protected int maximumSpeed;
	protected int speedLimit;
	protected int contaminationAlarmLimit;
	protected Weather weatherConditions;
	protected int totalContamination;
	private List <Vehicle> vehicles;
	
	// Creamos lista anonima.
		Comparator<Vehicle> locationOrder = new Comparator<Vehicle>() {

			@Override
			public int compare(Vehicle v1, Vehicle v2) {
				if(v2.getLocation() < v1.getLocation())
					return -1; // v2 anterior
				
				else if(v2.getLocation() > v1.getLocation())
					return 1;
				
				else return 0;
			}
		};
			
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		
		if(maxSpeed <= 0)
			throw new IllegalArgumentException("Argument maxSpeed must be positive.");
		else if(contLimit < 0) 
			throw new IllegalArgumentException("Argument contLimit must be positive or zero.");
		else if(length <= 0) 
			throw new IllegalArgumentException("Argument length must be positive.");
		else if(srcJunc == null || destJunc == null || weather == null) 
			throw new IllegalArgumentException("Arguments srJunc, destJunc or weather must be different from null");
		
		this.sourceJunction = srcJunc;
		this.destinationJunction = destJunc;
		this.maximumSpeed = maxSpeed;
		this.contaminationAlarmLimit = contLimit;
		this.length = length;
		this.weatherConditions = weather;
		this.speedLimit = maxSpeed;
		this.totalContamination = 0;
		
		this.vehicles = new SortedArrayList<Vehicle>(locationOrder); // No tengo claro si esto hay que hacerlo aqui
		
		// La constructora debe añadir la carretera como carretera saliente de su cruce origen, y como carretera entrante de su cruce destino.
		this.sourceJunction.addOutgoingRoad(this);
		this.destinationJunction.addIncommingRoad(this);
	}
	
	void enter(Vehicle v) {
		
		if(v.getLocation() != 0 || v.getSpeed() != 0) 
			throw new IllegalArgumentException("The location and speed of vehicle must be 0.");
		
		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) {
		if(w == null) 
			throw new IllegalArgumentException("The weather must not be null.");
		
		weatherConditions = w;
	}
	
	void addContamination(int c) {
		if(c < 0) 
			throw new IllegalArgumentException("Argument contamination must be positive.");
		
		totalContamination += c;
	}
	
	abstract void reduceTotalContamination(); //las clases de road lo implementan
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
	
	void advance(int time) {
		
		reduceTotalContamination();
		updateSpeedLimit();
		
		for(int i = 0; i < vehicles.size(); i++) {
			
			Vehicle v = vehicles.get(i);
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		
		vehicles.sort(locationOrder);
	}
	
	@Override
	public JSONObject report() {

		JSONObject json = new JSONObject();
		
		json.put("speedlimit", speedLimit);
		json.put("co2", totalContamination);
		json.put("weather", weatherConditions.toString()); 
		
		JSONArray ja = new JSONArray();
		for(int i = 0; i < vehicles.size(); i++)
			ja.put(vehicles.get(i).getId());
		
		json.put("vehicles", ja);
		json.put("id", super._id);
		
		return json;
	}
	
	public String getId() {
		return super._id;
	}
	
	public int getLength() {
		return length;
	}
	
	public Junction getSrc() {
		return sourceJunction;
	}
	
	public Junction getDest() {
		return destinationJunction;
	}
	
	public Weather getWeather() {
		return weatherConditions;
	}
	
	
	public int getContLimit() {
		return contaminationAlarmLimit;
	}
	
	public int getMaxSpeed() {
		return maximumSpeed;
	}
	public int getTotalCO2() {
		return totalContamination;
	}
	
	public int getCO2Limit() {
		return contaminationAlarmLimit;
	}
	
	public int getSpeedLimit() {
		return speedLimit;
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicles);
	}
}
