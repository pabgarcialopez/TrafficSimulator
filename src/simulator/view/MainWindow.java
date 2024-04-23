package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private Controller controller;

	public MainWindow(Controller controller) {
		super("Traffic Simulator");
		this.controller = controller;
		initGUI();
		
	}

	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		// Añadimos al mainPanel el Panel de Control y la Status Bar
		mainPanel.add(new ControlPanel(controller), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(controller), BorderLayout.PAGE_END);
		
		// Panel de las vistas general
		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		// Panel de las tablas que va dentro del panel de vistas.
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		

		viewsPanel.add(tablesPanel);
		
		// Panel de lo de la derecha.
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		// Events Table
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(controller)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		
		// Vehicles Table
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(controller)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		
		// Roads Table
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(controller)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);
		
		// Junctions Table
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(controller)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		// maps
		JPanel mapView = createViewPanel(new MapComponent(controller), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		
		// TODO add a map for MapByRoadComponent
		JPanel mapByRoad = createViewPanel(new MapByRoad(controller), "Map by road");
		mapByRoad.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoad);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel(new BorderLayout());
		
		Border defaultBorder = BorderFactory.createLineBorder(Color.black, 1);
		
		p.setBorder(BorderFactory.createTitledBorder(defaultBorder, title, TitledBorder.LEFT, TitledBorder.TOP));
		
		p.add(new JScrollPane(c));
		return p;
	}
}
