package simulator.model;

public class CityRoad extends Road{

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 2; // Por defecto
		
		if(weatherConditions == Weather.WINDY || 
		   weatherConditions == Weather.STORM) 
			x = 10;
		
		totalContamination -= x;
		
		if(totalContamination < 0)
			totalContamination = 0;
	}

	@Override
	void updateSpeedLimit() {
		speedLimit = maximumSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11 - v.getContClass())*speedLimit) / 11;
	}

}
