package simulator.view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private static final String WELCOME_MSG = "Welcome!";
	
	private Controller controller;
	private JLabel timeLabel;
	private JLabel messageLabel;
	private int time;
	
	public StatusBar(Controller controller) {
		this.controller = controller;
		this.controller.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setPreferredSize(new Dimension(1000, 40));
		
		this.timeLabel = new JLabel(" Time: " + String.valueOf(time));
		
		this.add(timeLabel);
		this.add(Box.createRigidArea(new Dimension(100, 30)));

		this.messageLabel = new JLabel(WELCOME_MSG);
		this.add(messageLabel);
	}
	
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		this.timeLabel.setText(" Time: " + String.valueOf(time));
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		messageLabel.setText("Event added (" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		timeLabel.setText(" Time: " + String.valueOf(time));
		messageLabel.setText(WELCOME_MSG);
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;
	}

	@Override
	public void onError(String err) {
		messageLabel.setText("ERROR: " + err);
		
	}

}
