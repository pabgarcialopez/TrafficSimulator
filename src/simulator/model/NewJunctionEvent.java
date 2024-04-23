package simulator.model;

public class NewJunctionEvent extends Event{
	
	int xCoor;
	int yCoor;
	String id;
	LightSwitchingStrategy lsStrategy;
	DequeuingStrategy dqStrategy;
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
        super(time);
        this.id  = id;
        this.xCoor = xCoor;
    	this.yCoor = yCoor;
    	this.lsStrategy  = lsStrategy;
    	this.dqStrategy = dqStrategy;
    	
	}

	@Override
	void execute(RoadMap map) {
		map.addJunction(new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor));
	}
	@Override
	public String toString() {
		return "New Junction '" + id + "'";
	}
}
