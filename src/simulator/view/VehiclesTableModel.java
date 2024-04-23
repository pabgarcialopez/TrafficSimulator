package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final String[] columns = {"Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance"};

	private Controller controller;
	private List<Vehicle> vehicles;

	public VehiclesTableModel(Controller controller) {
		this.controller = controller;
		this.controller.addObserver(this);
	}
	
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.vehicles = map.getVehicles();	
		fireTableDataChanged();

	}

	@Override
	public void onError(String err) {
		
		
	}

	@Override
	public int getRowCount() {
		return vehicles == null ? 0 : vehicles.size();
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
		
			case 0: // id del vehiculo de indice rowIndex
				s = vehicles.get(rowIndex).toString();
				break;
				
			case 1: // Location o estado del vehiculo de indice rowIndex
				switch(vehicles.get(rowIndex).getStatus()) {
					case PENDING: 
						s = "Pending"; 
						break;
						
					case TRAVELING: 
						s = vehicles.get(rowIndex).getRoad().toString() + ":" + vehicles.get(rowIndex).getLocation();
						break;
						
					case WAITING: 
						s = "Waiting:" + vehicles.get(rowIndex).getRoad().getDest().toString();
					break;
					
					case ARRIVED:
						s = "Arrived";
					break;
				}
				
				break;
				
			case 2: // Itinerario del vehiculo de indice rowIndex
				s = vehicles.get(rowIndex).getItinerary().toString(); // Deberia ponerlo en el formato correcto.
				break;
				
			case 3: // CO2 Class
				s = vehicles.get(rowIndex).getContClass();
				break;
			
			case 4: // Max Speed
				s = vehicles.get(rowIndex).getMaxSpeed();
				break;
			
			case 5: // Speed
				s = vehicles.get(rowIndex).getSpeed();
				break;
				
			case 6: // Total CO2
				s = vehicles.get(rowIndex).getTotalCO2();
				break;
			
			case 7: // Distance
				s = vehicles.get(rowIndex).getLocation();
				break;
		}
		
		return s;
	}

}
