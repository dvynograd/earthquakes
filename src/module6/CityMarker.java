package module6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 */
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 5;  // The size of the triangle marker
	
	private boolean isShowStatistic = false;
	
	public boolean isShowStatistic() {
		return isShowStatistic;
	}

	public void setShowStatistic(boolean isShowStatistic) {
		this.isShowStatistic = isShowStatistic;
	}


	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}
	
	
	// pg is the graphics object on which you call the graphics
	// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
	// x and y are the center of the object to draw. 
	// They will be used to calculate the coordinates to pass
	// into any shape drawing methods.  
	// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
	// whose upper left corner is at position x, y
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		//System.out.println("Drawing a city");
		// Save previous drawing style
		pg.pushStyle();
		
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		if (isShowStatistic == true) {
			showStatistic(pg, x, y);
		}
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	private void showStatistic(PGraphics pg, float x, float y) {
		pg.fill(255, 250, 240);
		pg.rect(x + 10, y, 300, 100);
		pg.textAlign(pg.LEFT, pg.CENTER);
		pg.textSize(12);
		pg.fill(0);
		EarthquakeMarker[] earthquakes = getEarthquakesInThreatCircle();
		int count = earthquakes != null ? earthquakes.length : 0;
		pg.text("in threat circle of " + count + " earthquakes", x + 10, y + 15);
		if (earthquakes != null) {
			pg.text("The average magnitude is " + average(earthquakes) + " earthquakes", x + 10, y + 30);
			pg.text("The most magnitude was " + earthquakes[0].getMagnitude() + " earthquakes", x + 10, y + 45);
			pg.text("The maximun depth was " + maxDepth(earthquakes), x + 10, y + 60);
		}
	}


	private Float maxDepth(EarthquakeMarker[] earthquakes) {
		Arrays.sort(earthquakes, EarthquakeMarker.depthComparator);
		return earthquakes[0].getDepth();
	}

	private int average(EarthquakeMarker[] earthquakes) {
		float sum = 0;
		for (EarthquakeMarker quake:earthquakes) {
			sum += quake.getMagnitude();
		}
		return (int)(sum / earthquakes.length);
	}

	private EarthquakeMarker[] getEarthquakesInThreatCircle() {
		List<EarthquakeMarker> quakesList = new ArrayList<EarthquakeMarker>();
		MarkerFactory markerFactory = new MarkerFactory();
		for (Marker marker: markerFactory.getQuakes()) {
			EarthquakeMarker quake = (EarthquakeMarker) marker;
			if (quake.isInThreatCircle(this)) {
				quakesList.add(quake);
			}
		}
		EarthquakeMarker[] quakeArray = null;
		if (quakesList.size() > 0) {
			quakeArray = new EarthquakeMarker[quakesList.size()];
			quakesList.toArray(quakeArray);
			Arrays.sort(quakeArray);
		}
		
		return quakeArray;
	}

	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCity() + " " + getCountry() + " ";
		String pop = "Pop: " + getPopulation() + " Million";
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(pop)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(pop, x+3, y - TRI_SIZE -18);
		
		pg.popStyle();
	}
	
	private String getCity()
	{
		return getStringProperty("name");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
