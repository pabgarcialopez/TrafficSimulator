package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final String[] columns = {"Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit"};

	private Controller controller;
	private List<Road> roads;

	public RoadsTableModel(Controller controller) {
		this.controller = controller;
		this.controller.addObserver(this);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		
		
	}

	@Override
	public int getRowCount() {
		
		return roads == null ? 0 : roads.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return columns[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Object s = null;
		
		switch (columnIndex) {
		
			case 0: // id de la carretera de indice rowIndex
				s = roads.get(rowIndex).toString();
				break;
				
			case 1: // Longitud de la carretera de indice rowIndex
				s = roads.get(rowIndex).getLength();
				break;
				
			case 2: // Clima de la carretera de indice rowIndex
				s = roads.get(rowIndex).getWeather();
				break;
				
			case 3: // Max Speed
				s = roads.get(rowIndex).getMaxSpeed();				
				break;
			
			case 4: // Max Speed
				s = roads.get(rowIndex).getSpeedLimit();
				break;
				
			case 5: // Total CO2
				s = roads.get(rowIndex).getTotalCO2();
				break;
			
			case 6: // CO2 Limit
				s = roads.get(rowIndex).getCO2Limit();
				break;
		}
		
		return s;
	}

}
