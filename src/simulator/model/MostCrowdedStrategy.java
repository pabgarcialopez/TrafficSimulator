package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{

	private int timeSlot;
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if(roads.size() == 0) 
			return -1;	
		
		else if(currGreen == -1)
			return searchNextGreen(qs, 0);
		
		else if(currTime - lastSwitchingTime < timeSlot) 
			return currGreen;
		
		else return searchNextGreen(qs, (currGreen+1)%roads.size());
	}
	
	private int searchNextGreen(List<List<Vehicle>> qs, int startIndex) {
		int max = qs.get(startIndex).size();
		int maxIndex = startIndex;
		
		// Busqueda circular
		int i = (startIndex + 1) % qs.size();
		
		while(i != startIndex) {
			int s = qs.get(i).size();
			if(s > max) {
				max = s;
				maxIndex = i;
			}
			i = (i+1) % qs.size();
		}
		return maxIndex; 
	}

}
