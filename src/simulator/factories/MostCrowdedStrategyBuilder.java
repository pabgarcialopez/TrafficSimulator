package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot = 1;
		
		if(data.has("timeslot")) 
			timeslot = data.getInt("timeslot");
		
		LightSwitchingStrategy ls = new MostCrowdedStrategy(timeslot);
		return ls;
	}

}
