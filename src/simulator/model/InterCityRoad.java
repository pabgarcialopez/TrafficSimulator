package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		switch(weatherConditions) {
			case SUNNY:  x = 2;  break;
			case CLOUDY: x = 3;  break;
			case RAINY:  x = 10; break;
			case WINDY:  x = 15; break;
			case STORM:  x = 20; break;
		};
		
		int tc = totalContamination;	
		totalContamination = ((100 - x) * tc )/100;
	}

	@Override
	void updateSpeedLimit() {
		if(totalContamination > contaminationAlarmLimit)
			this.speedLimit = maximumSpeed / 2 ;
		
		else this.speedLimit = maximumSpeed; 
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(weatherConditions == Weather.STORM) 
			return (speedLimit * 8)/10;
		else return speedLimit;
	}
	

}
