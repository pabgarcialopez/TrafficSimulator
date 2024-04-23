package simulator.model;

import simulator.misc.Pair;
import java.util.List;

public class SetContClassEvent extends Event{
	List<Pair<String,Integer>> cs; 
	
	// Pares vehiculo(String) - co2Class(Integer).
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) {
        super(time);
        
        if(cs == null) 
        	throw new IllegalArgumentException("cs must not be null");
        
        this.cs = cs;
    }
	
	@Override
	void execute(RoadMap map) {
		for(int i = 0; i< cs.size(); i++) {
			Vehicle v = map.getVehicle(cs.get(i).getFirst());
			if(v == null) 
				throw new IllegalArgumentException("This vehicle doesn't exist.");
			v.setContClass(cs.get(i).getSecond());
		}
	}
	
	@Override
	public String toString() {
		
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("Change CO2 class: [");
		
		for(int i = 0; i < cs.size(); i++) {
			buffer.append("(");
			buffer.append(cs.get(i).getFirst());
			buffer.append(",");
			buffer.append(cs.get(i).getSecond());
			buffer.append(")");
			
			// Para la separacion entre varios.
			if(i != cs.size() - 1)
				buffer.append(", ");
		}
		
		buffer.append("]");
		
		return buffer.toString();
	}
}
