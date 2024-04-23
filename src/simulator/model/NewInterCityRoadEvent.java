package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
       super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
    }
	
	@Override
	Road createRoadObject() {
		return new InterCityRoad(id, srcJunc, destJunc, maxSpeed, co2Limit, length, weather);
		
	}
	
	@Override
	public String toString() {
		return "New InterCityRoad '" + id + "'";
	}
}
