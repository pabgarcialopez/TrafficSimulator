package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel<Road> roads;
	private JComboBox<Road> roadsBox;
	private Road selectedRoad;
	private Weather selectedWeather;
	private Integer selectedTime;
	private int status;
	
	public ChangeWeatherDialog(JFrame frame) {
		super(frame, "Change Road Weather", true);
		this.selectedRoad = null;
		this.selectedWeather = null;
		this.selectedTime = null;
		this.roads = new DefaultComboBoxModel<Road>();
		this.roadsBox = new JComboBox<Road>(this.roads);
		
		initGUI();
	}

	private void initGUI() {
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JComboBox<Weather> weatherBox = new JComboBox<Weather>();
		Weather[] weathers = Weather.values();
		for(Weather w: weathers)
			weatherBox.addItem(w);
		
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1)); // ini, min, max, step
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		// Mensaje
		JLabel label = new JLabel("Schedule an event to change the weather "
				+ "of a road after a given number of simulation ticks from now.");
		labelPanel.add(label);
		
		
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new FlowLayout());
		
		// Labels para los JCombos
		comboPanel.add(new JLabel("Road: "));
		comboPanel.add(roadsBox);
		
		comboPanel.add(new JLabel("Weather: "));
		comboPanel.add(weatherBox);
		
		comboPanel.add(new JLabel("Ticks: "));
		comboPanel.add(ticksSpinner);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		// Botones Cancel y OK
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		
		cancelButton.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeWeatherDialog.this.status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
			
		});
		
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		
		okButton.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ChangeWeatherDialog.this.status = 1;
				
				ChangeWeatherDialog.this.selectedRoad = (Road) roadsBox.getSelectedItem();
				ChangeWeatherDialog.this.selectedTime = (Integer) ticksSpinner.getValue();
				ChangeWeatherDialog.this.selectedWeather = (Weather) weatherBox.getSelectedItem();
				
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
	
		mainPanel.add(labelPanel, BorderLayout.NORTH);
		mainPanel.add(comboPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel);
	
	}
	
	public int open(RoadMap map) {
		
		List<Road> roads = map.getRoads();
		this.roads.removeAllElements();
		
		for(Road r: roads)
			this.roads.addElement(r);
		
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
		pack();
		setVisible(true);
		
		return this.status;
	}
	
	Road getSelectedRoad() {
		return this.selectedRoad;
	}
	
	Weather getSelectedWeather() {
		return this.selectedWeather;
	}

	Integer getSelectedTime() {
		return this.selectedTime;
	}
}
