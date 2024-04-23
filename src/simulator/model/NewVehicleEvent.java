package simulator.model;

import java.util.List;
import java.util.ArrayList;

public class NewVehicleEvent extends Event {
	
	String id;
	int maxSpeed;
	int contClass;
	List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
        super(time);
        this.id = id;
        this.maxSpeed = maxSpeed;
        this.contClass = contClass;
        this.itinerary = itinerary;
	}
	
	@Override
	void execute(RoadMap map) {
		List<Junction> trueItinerary = new ArrayList<Junction>();
		
		for(int i = 0; i < itinerary.size(); i++)
			trueItinerary.add(map.getJunction(itinerary.get(i)));		
		
		Vehicle v = new Vehicle(id, maxSpeed, contClass, trueItinerary);
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	@Override
	public String toString() {
		return "New Vehicle '" + id + "'";
	}
}
