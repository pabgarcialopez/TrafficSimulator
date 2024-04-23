package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	Factory <LightSwitchingStrategy> lssFactory;
	Factory <DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder (Factory <LightSwitchingStrategy> lssFactory, Factory <DequeuingStrategy> dqsFactory) {
		super("new_junction"); 
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		int time = data.getInt("time");
		String id = data.getString("id");
		
		JSONArray coor = data.getJSONArray("coor");
		int xCoor = coor.getInt(0);
		int yCoor = coor.getInt(1);
		
		JSONObject lsStrategy = data.getJSONObject("ls_strategy");
		LightSwitchingStrategy ls = lssFactory.createInstance(lsStrategy);
		
		JSONObject dqStrategy = data.getJSONObject("dq_strategy");
		DequeuingStrategy dq = dqsFactory.createInstance(dqStrategy);

		return new NewJunctionEvent(time, id, ls, dq, xCoor, yCoor);
	}

}
