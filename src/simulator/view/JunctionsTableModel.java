package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final String[] columns = {"Id", "Green", "Queues"};

	private Controller controller;
	private List<Junction> junctions;

	public JunctionsTableModel(Controller controller) {
		this.controller = controller;
		this.controller.addObserver(this);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}
	
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		fireTableDataChanged();
	}
	
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		fireTableDataChanged();
	}
	
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.junctions = map.getJunctions();
		fireTableDataChanged();
	}
	
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.junctions = map.getJunctions();
		fireTableDataChanged();

		
	}
	
	@Override
	public void onError(String err) {
		
		
	}

	@Override
	public int getRowCount() {
		return junctions == null ? 0 : junctions.size();
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
		
			case 0: // id del cruce de indice rowIndex
				s = junctions.get(rowIndex).toString();
				break;
				
			case 1: // Green del cruce de indice rowIndex
				int greenLightIndex = junctions.get(rowIndex).getGreenLightIndex();
				
				if(greenLightIndex == -1) 
					s = "NONE";
				else s = junctions.get(rowIndex).getInRoads().get(greenLightIndex); // Esto no estoy seguro de si esta bien.
				
				break;
				
			case 2: // Colas del cruce de indice rowIndex
				
				String queues = "";
				
				// Carreteras entrantes.
				List<Road> inRoads = junctions.get(rowIndex).getInRoads();
				
				
				// inRoads.get(i).toString() me da el identificador de la carretera (r1 por ejemplo)
				// inRoads.get(i).getVehicles().toString() me da la lista de vehiculos en el formato [v1, v2, ...]
				for(int i = 0; i < inRoads.size(); i++) {
					
					List<Vehicle> vehiclesQueue = junctions.get(rowIndex).getQueue(inRoads.get(i));
					
					queues = queues + inRoads.get(i).getId() + ":" + vehiclesQueue + " ";
					
					if(i != inRoads.size() - 1)
						queues = queues + " ";
				}
					
				s = queues;
				break;
		}
		
		return s;
	}
}

