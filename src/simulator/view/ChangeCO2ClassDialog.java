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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private JComboBox<Vehicle> vehiclesBox;
	private DefaultComboBoxModel<Vehicle> vehicles;
	
	private Vehicle selectedVehicle;
	private Integer selectedCO2;
	private int status;
	private Integer selectedTime;
	
	public ChangeCO2ClassDialog(JFrame frame) {
		super(frame, "Change CO2 Class", true);
		this.selectedVehicle = null;
		this.selectedCO2 = null;
		this.selectedTime = null;
		this.vehicles = new DefaultComboBoxModel<>();
		this.vehiclesBox = new JComboBox<Vehicle>(vehicles);
		
		initGUI();
	}
	
	private void initGUI() {
				
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
			
		JComboBox<Integer> co2ClassBox = new JComboBox<Integer>();
		for(int i = 0; i <= 10; i++)
			co2ClassBox.addItem(i);
		
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1)); // ini, min, max, step
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		// Mensaje de la parte superior del dialogo
		JLabel label = new JLabel("Schedule an event to change the weather of a road "
				+ "class of a vehicle after a given number of simulation ticks from now.");
		labelPanel.add(label, BorderLayout.NORTH);
		
		
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new FlowLayout());
		
		// Labels para los JCombos
		comboPanel.add(new JLabel("Vehicles: "));
		comboPanel.add(vehiclesBox);
		
		comboPanel.add(new JLabel("CO2 Class: "));
		comboPanel.add(co2ClassBox);
		
		comboPanel.add(new JLabel("Ticks: "));
		comboPanel.add(ticksSpinner);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		// Botones Cancel
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
				
			}
		});
		
		// Boton OK
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				ChangeCO2ClassDialog.this.status = 1;
				
				ChangeCO2ClassDialog.this.selectedTime = (Integer) ticksSpinner.getValue();
				ChangeCO2ClassDialog.this.selectedVehicle = (Vehicle) vehiclesBox.getSelectedItem();
				ChangeCO2ClassDialog.this.selectedCO2 = (Integer) co2ClassBox.getSelectedItem();
				
				ChangeCO2ClassDialog.this.setVisible(false);
			}
			
		});
		
		mainPanel.add(labelPanel, BorderLayout.NORTH);
		mainPanel.add(comboPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.add(mainPanel);
	}
	
	public int open(RoadMap map) {
		
		List<Vehicle> vehicles = map.getVehicles();
		this.vehicles.removeAllElements();
		
		for(Vehicle v: vehicles)
			this.vehicles.addElement(v);
		
		setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
		pack();
		setVisible(true);

		return status;
	}
	
	Vehicle getSelectedVehicle() {
		return this.selectedVehicle;
	}
	
	Integer getSelectedCO2Class() {
		return this.selectedCO2;
	}
	
	Integer getSelectedTime() {
		return this.selectedTime;
	}
}
