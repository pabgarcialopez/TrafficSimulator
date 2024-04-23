package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class MapByRoad extends JComponent implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	
	private static final Color _BLACK_COLOR = Color.BLACK;
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final int _JRADIUS = 10;
	private Image _car;

	private RoadMap _map;
	
	public MapByRoad(Controller controller) {
		initGUI();
		controller.addObserver(this);
	}
	
	public void initGUI() {
		setPreferredSize (new Dimension (300, 200));
		_car = loadImage("car.png");
	}
	
	public void paintComponent(Graphics graphics) {
		// Estas cuatro primeras lineas son copiar y pegar.
		// Las segundas dos lineas son para conseguir mejor definicion.
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());
		
		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		
		List<Road> roads = _map.getRoads();
		for(int i = 0; i < roads.size(); i++) {
			
			// Posiciones
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i+1)*50;	
			
			g.setColor(_BLACK_COLOR);
			g.drawString(roads.get(i).getId(), x1 - 30, y);
			//draw the line
			g.drawLine(x1, y, x2, y);
			
			// Añadimos el circulo del principio de la carretera
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(roads.get(i).getSrc().getId(), x1, y-10);
			
			// choose a color for the final junction depending on the traffic light of the road
			Color destJunctionColor = _RED_LIGHT_COLOR;
			int idx = roads.get(i).getDest().getGreenLightIndex();
			if (idx != -1 && roads.get(i).equals(roads.get(i).getDest().getInRoads().get(idx))) {
				destJunctionColor = _GREEN_LIGHT_COLOR;
			}
			
			// Añadimos el circulo del final de la carretera con su color correspondiente
			g.setColor(destJunctionColor);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(roads.get(i).getDest().getId(), x2, y-10);
			List<Vehicle> vehiclesOnTheRoad = roads.get(i).getVehicles();
			
			for(int j = 0; j < vehiclesOnTheRoad.size(); j++) {
				int x = x1 + (int) ((x2-x1)*((double) vehiclesOnTheRoad.get(j).getLocation() / (double) roads.get(i).getLength()));
				g.drawImage(_car,x, y-10, 16, 16, this);
				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) vehiclesOnTheRoad.get(j).getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				g.drawString(vehiclesOnTheRoad.get(j).getId(), x, y-12);
			}
			
			Weather weatherRoad = roads.get(i).getWeather();
			Image weather = null;
			switch(weatherRoad) {
				case STORM: weather = loadImage("storm.png");
					break;
				case SUNNY: weather = loadImage("sun.png");
					break;
				case WINDY: weather = loadImage("wind.png");
					break;
				case CLOUDY: weather = loadImage("cloud.png");
					break;
				case RAINY: weather = loadImage("rain.png");
					break;
			}
			
			g.drawImage(weather, x2 + 10, y-16, 32, 32, this);
			
			int C = (int) Math.floor(Math.min((double) roads.get(i).getTotalCO2()/(1.0 + (double) roads.get(i).getCO2Limit()),1.0) / 0.19);
			String file_cont = "cont_" + C + ".png";
			Image cont = loadImage(file_cont);
			
			g.drawImage(cont, x2+48, y - 16, 32, 32, this);
		}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}
	
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {}
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}


	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
}
