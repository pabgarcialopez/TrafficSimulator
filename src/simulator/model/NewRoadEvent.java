package simulator.model;

public abstract class NewRoadEvent extends Event{
	
	String id;
	
	Junction srcJunc;
	String srcJuncString;
	
	Junction destJunc;
	String destJuncString;
	
	int length;
	int co2Limit;
	int maxSpeed;
	Weather weather;
	
	public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
       super(time);
       this.id = id;
       this.srcJuncString = srcJunc;
       this.destJuncString = destJunc;
       this.length = length;
       this.co2Limit = co2Limit;
       this.maxSpeed = maxSpeed;
       this.weather = weather;

    }


	@Override
	void execute(RoadMap map) {
		this.srcJunc = map.getJunction(srcJuncString);
		this.destJunc = map.getJunction(destJuncString);
		map.addRoad(createRoadObject());
		
	}
	
	@Override
	public String toString() {
		return "New Road '" + id + "'";
	}
	
	abstract Road createRoadObject();
}
