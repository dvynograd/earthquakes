package module6;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class MarkerFactory {

	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	//private String earthquakesURL = "quiz2.atom";
	// private String earthquakesURL = "test1.atom";
	// private String earthquakesURL = "test2.atom";
	// private String earthquakesURL = "2.5_week.atom"; // The same feed, but saved
	// August 7, 2015

	static private List<Marker> earthquakes = null;

	static private List<Marker> cities = null;

	static private List<Marker> countryMarkers = null;
	
	static private List<Marker> airports = null;

	private String countryFile = "countries.geo.json";
	
	private String aiportSource = "airports.dat";

	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";

	public List<Marker> getCities() {
		if (MarkerFactory.cities == null) {
			List<Feature> citiesFeatures = GeoJSONReader.loadData(new PApplet(), cityFile);
			MarkerFactory.cities = new ArrayList<Marker>();
			for(Feature city : citiesFeatures) {
				MarkerFactory.cities.add(new CityMarker(city));
			}

		}
		return MarkerFactory.cities;
	}

	public List<Marker> getQuakes() {
		if (earthquakes == null) {
			List<PointFeature> earthquakes = ParseFeed.parseEarthquake(new PApplet(), earthquakesURL);
			MarkerFactory.earthquakes = new ArrayList<Marker>();
			for (PointFeature feature : earthquakes) {
				if (isLand(feature)) {
					MarkerFactory.earthquakes.add(new LandQuakeMarker(feature));
				} else {
					MarkerFactory.earthquakes.add(new OceanQuakeMarker(feature));
				}
			}
		}
		return MarkerFactory.earthquakes;
	}

	public List<Marker> getCountryMarkers() {
		if (countryMarkers == null) {
			List<Feature> countries = GeoJSONReader.loadData(new PApplet(), countryFile);
			countryMarkers = MapUtils.createSimpleMarkers(countries);
		}
		return countryMarkers;

	}
	
	public List<Marker> getAirportMarkers() {
		if (countryMarkers == null) {
			//List<Feature> countries = ParseFeed.parseAirports(new PApplet(), aiportSource);
			//countryMarkers = MapUtils.createSimpleMarkers(countries);
		}
		return airports;

	}

	// Checks whether this quake occurred on land. If it did, it sets the
	// "country" property of its PointFeature to the country where it occurred
	// and returns true. Notice that the helper method isInCountry will
	// set this "country" property already. Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		for (Marker country : getCountryMarkers()) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}

		// not inside any country
		return false;
	}

	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake
	// feature if
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if (country.getClass() == MultiMarker.class) {

			// looping over markers making up MultiMarker
			for (Marker marker : ((MultiMarker) country).getMarkers()) {

				// checking if inside
				if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));

					// return if is inside one
					return true;
				}
			}
		}

		// check if inside country represented by SimplePolygonMarker
		else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));

			return true;
		}
		return false;
	}

}
