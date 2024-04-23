package simulator.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final int INITIAL_TICKS = 10;
	private static final int MAX_PERMITTED_TICKS = 10000;
	private static final int MIN_PERMITTED_TICKS = 1;
	private static final int TICKS_STEP = 1;
	
	private JToolBar barra;
	private JFileChooser fileChooser;
	private JSpinner ticksSpinner;
	private Controller controller;
	
	
	
	// Estos son los que se van a pasar.
	private boolean _stopped; // No se si va aqui o que
	private int time;
	private RoadMap map;
	private List<JButton> buttonsInToolBar;
	
	public ControlPanel(Controller controller) {
		this.controller = controller;
		
		this._stopped = true;
		this.time = 0;
		this.map = null;
		this.buttonsInToolBar = new ArrayList<JButton>();
		
		initGUI();
		
		this.controller.addObserver(this);
	}
	
	private void initGUI() {
		
		this.barra = new JToolBar();
		this.add(barra);
		this.barra.setPreferredSize(new Dimension(1000, 40));
		
		initOpenFileButton();
		
		this.barra.addSeparator();

		initChangeCO2ClassButton();
		initChangeWeatherButton();
		
		this.barra.addSeparator();
		
		initRunButton();
		
		initStopButton();
		
		initTicksSpinner();
		
		this.barra.add(Box.createGlue());
		
		this.barra.addSeparator();
	
		initExitButton();
		
		// Hacemos visible el panel
		this.setVisible(true);
	}

	private void initOpenFileButton() {
		
		this.fileChooser = new JFileChooser();
		JButton openFileButton = new JButton(); 
		openFileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		openFileButton.setToolTipText("Opens an existing file from system");
		fileChooser.setCurrentDirectory(new File("resources/examples"));
		this.barra.add(openFileButton);
		this.buttonsInToolBar.add(openFileButton);
		
		openFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doOpenFileAction();
			}
			
		});
	}
	
	private void initChangeCO2ClassButton() {
		
		JButton changeCO2ClassButton = new JButton();
		changeCO2ClassButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
		changeCO2ClassButton.setToolTipText("Changes vehicle's CO2 class");
		this.barra.add(changeCO2ClassButton);
		this.buttonsInToolBar.add(changeCO2ClassButton);
		
		changeCO2ClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doChangeCO2Action();
			}
			
		});
	}

	private void initChangeWeatherButton() {
		JButton changeWeatherButton = new JButton();
		changeWeatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		changeWeatherButton.setToolTipText("Changes road's weather");
		this.barra.add(changeWeatherButton);
		this.buttonsInToolBar.add(changeWeatherButton);
		
		changeWeatherButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doChangeWeatherAction();
			}
			
		});
	}
	
	private void initRunButton() {
		JButton runButton = new JButton();
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		runButton.setToolTipText("Starts simulation");
		this.barra.add(runButton);
		this.buttonsInToolBar.add(runButton);
		
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = false;
				enableToolBar(false);
				run_sim((int) ticksSpinner.getValue());
			}
			
		});
	}
	
	private void initStopButton() {
		JButton stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.setToolTipText("Pauses simulation");
		this.barra.add(stopButton);
		
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
			
		});
	}
	
	private void initTicksSpinner() {
		ticksSpinner = new JSpinner(new SpinnerNumberModel(INITIAL_TICKS, MIN_PERMITTED_TICKS, MAX_PERMITTED_TICKS, TICKS_STEP));
		ticksSpinner.setToolTipText("Simulation ticks to run: " + MIN_PERMITTED_TICKS + " - " + MAX_PERMITTED_TICKS);
		ticksSpinner.setMaximumSize(new Dimension(100, 30));
		ticksSpinner.setPreferredSize(new Dimension(100, 30));
		barra.add(new JLabel(" Ticks: "));
		barra.add(ticksSpinner);
	}
	
	private void initExitButton() {
		
		JButton exitButton = new JButton();
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.setToolTipText("Exit simulation");
		this.barra.add(exitButton);
		this.buttonsInToolBar.add(exitButton);
		
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int n = JOptionPane.showOptionDialog(barra,
						"Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				
				if(n == 0)
					System.exit(0);
			}
			
		});
	}
	
	
	private void doOpenFileAction() {
		int answer = fileChooser.showOpenDialog(this);
		
		// Si se carga un archivo (se presiona open):
		if (answer == JFileChooser.APPROVE_OPTION) {
			controller.reset();
			
			try {
				InputStream in = new FileInputStream(fileChooser.getSelectedFile());
				controller.loadEvents(in);
			}
			
			catch(Exception ioe) {
				showError(ioe.getMessage()); //error de parseo.
				//no podemos llamar al onError porque estamos en la vista, solo el Modelo puede llamar a onError, por eso lo llamamos showError.
			}
		}		
	}
	
	private void doChangeCO2Action() {
		ChangeCO2ClassDialog changeCO2Dialog = new ChangeCO2ClassDialog((JFrame) SwingUtilities.getWindowAncestor(this));
		int status = changeCO2Dialog.open(map);
		
		if(status == 1) {
			
			Vehicle selectedVehicle = changeCO2Dialog.getSelectedVehicle();
			
			if(selectedVehicle != null) {
				
				Integer selectedTime = changeCO2Dialog.getSelectedTime();
				Integer selectedCO2Class = changeCO2Dialog.getSelectedCO2Class();
				
				List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
				list.add(new Pair<String, Integer>(selectedVehicle.toString(), selectedCO2Class));
				controller.addEvent(new SetContClassEvent(time + selectedTime, list));
			}
			
			else JOptionPane.showMessageDialog(null, "No vehicles available.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void doChangeWeatherAction() {
		ChangeWeatherDialog changeWeatherDialog = new ChangeWeatherDialog((JFrame) SwingUtilities.getWindowAncestor(this));
		int status = changeWeatherDialog.open(map);
		
		if(status == 1) {
			
			Road selectedRoad = changeWeatherDialog.getSelectedRoad();
		
			if(selectedRoad != null) {
				Integer selectedTime = changeWeatherDialog.getSelectedTime();
				Weather selectedWeather = changeWeatherDialog.getSelectedWeather();
				List<Pair<String,Weather>> list = new ArrayList<Pair<String,Weather>>();
				
				list.add(new Pair<String, Weather>(selectedRoad.toString(), selectedWeather));
				controller.addEvent(new SetWeatherEvent(time + selectedTime, list));
			}
			
			else JOptionPane.showMessageDialog(null, "No roads available.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void run_sim(int n) {
		
		if (n > 0 && !_stopped) {
			
			try {
				controller.run(1);
			} catch (Exception e) {
				showError(e.getMessage());
				enableToolBar(true);
				_stopped = true;
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
			
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void enableToolBar(boolean enable) {
		for(JButton button: buttonsInToolBar) {
			button.setEnabled(enable);
		}
	}

	private void stop() {
		_stopped = true;
	}
	

	public void showError(String err) {
		JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.map = map;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.map = map;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.map = map;

		ticksSpinner.setValue(INITIAL_TICKS);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.time = time;
		this.map = map;
	}
	
	@Override
	public void onError(String err) {}
	
}
