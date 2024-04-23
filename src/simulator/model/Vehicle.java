package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject{

	private List <Junction> itinerary; // Lista de cruces que representa el itinerario del vehiculo.
	private int maximumSpeed;
	private int currentSpeed;
	private VehicleStatus status;
	private Road road; // Debe ser null en caso de que no este en ninguna carretera.
	private int location;
	private int contaminationClass; // 0-10
	private int totalContamination;
	private int totalTravelledDistance;
	private int nextRoadIndex; 
	
	
	Vehicle(String id, int maxSpeed, int contClass, List <Junction> itinerary)  {
		
		super(id);
		
		if(maxSpeed <= 0) 
			throw new IllegalArgumentException("Argument maxSpeed must be positive.");
		else if(contClass < 0 || contClass > 10) 
			throw new IllegalArgumentException("Argument contClass must be between 0 and 10.");
		else if(itinerary.size() < 2)
			throw new IllegalArgumentException("Argument itinerary must have at least 2 elements.");
		
		this.itinerary = Collections.unmodifiableList(new ArrayList<> (itinerary));
		this.maximumSpeed = maxSpeed;
		this.currentSpeed = 0;
		this.nextRoadIndex = 0;
		this.location = 0;
		this.contaminationClass = contClass;
		this.totalContamination = 0;
		this.road = null;
		this.status = VehicleStatus.PENDING;
	}

	@Override
	void advance(int time) {
		
		if(status == VehicleStatus.TRAVELING) {
			
			// Actualizamos posicion
			int oldLocation = location;
			location = Math.min(location + currentSpeed, road.getLength());
			totalTravelledDistance += location - oldLocation;
			
			// Actualizamos contaminacion
			int c = (location - oldLocation) * contaminationClass;
			totalContamination += c;
			road.addContamination(c);
			
			if(location == road.getLength()) { 
				road.getDest().enter(this);
				nextRoadIndex++;
				status = VehicleStatus.WAITING;
				currentSpeed = 0;
			}
		}
	}
	
	void moveToNextRoad() {
		
		
		if(status != VehicleStatus.WAITING && status != VehicleStatus.PENDING)
			throw new IllegalArgumentException("Vehicle must be waiting or pending.");
		
		if(road != null)
			road.exit(this);
		
		if(nextRoadIndex != itinerary.size() - 1) {
			
			road = itinerary.get(nextRoadIndex).roadTo(itinerary.get(nextRoadIndex + 1));
			location = 0;
			status = VehicleStatus.TRAVELING;
			road.enter(this);
		}
		
		else {
			status = VehicleStatus.ARRIVED;
			road = null;
			location = 0;
		}
	}

	@Override
	public JSONObject report() {
		
		JSONObject json = new JSONObject();
				
		json.put("distance", totalTravelledDistance); 
		json.put("id", super._id);
		json.put("class", contaminationClass);
		json.put("speed", currentSpeed); 
		json.put("status", status.toString());
		json.put("co2", totalContamination);
		
		if(status != VehicleStatus.PENDING && status != VehicleStatus.ARRIVED) {
			json.put("road", road.getId());
			json.put("location", location);
		}
		
		return json;
	}
	
	void setSpeed(int s) {
		
		 
		if(s < 0) 
			throw new IllegalArgumentException("Speed must be positive.");
		
		if(status == VehicleStatus.TRAVELING)
			this.currentSpeed = Math.min(s, this.maximumSpeed);
		
	}
	
	void setContClass(int c) {
		
		if(c < 0 || c > 10) 
			throw new IllegalArgumentException("Contamination must be between 0 and 10.");
	
		this.contaminationClass = c;
	}

	public int getSpeed() {
		return currentSpeed;
	}
	
	public int getLocation() {
		return location;
	}
	
	public int getContClass() {
		return contaminationClass;
	}
	
	public String toString() {
		return super._id;
	}

	public Road getRoad() {
		return road;
	}
	public List<Junction> getItinerary(){
		return itinerary;
	}
	
	public int getMaxSpeed() {
		return maximumSpeed;
	}
	
	public VehicleStatus getStatus() {
		return status;
	}
	
	public int getTotalCO2() {
		return totalContamination;
	}
	
}
