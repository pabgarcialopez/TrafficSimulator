package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final String[] columns = {"Time", "Desc."};
	
	private Controller controller;
	private List<Event> events;
	
	public EventsTableModel(Controller controller) {
		
		this.controller = controller;
		this.controller.addObserver(this);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {}

	@Override
	public int getRowCount() {
		return events == null ? 0 : events.size();
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
		
			case 0: // Tiempo del evento de indice rowIndex
				s = events.get(rowIndex).getTime();
				break;
				
			case 1: // Descripcion del evento de indice rowIndex
				s = events.get(rowIndex).toString();
				break;
		}
		
		return s;
	}
}
