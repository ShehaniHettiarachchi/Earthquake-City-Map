package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 100, 100, 1800, 800, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 100, 100, 1800, 800, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    /*These print statements show : (1)All of the relevent properties in the features &
	     * (2)How to get one property and use it*/
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	
	    	//PointFeature also have a getLocation method
	    }
	    for(PointFeature ia: earthquakes) {
	         SimplePointMarker to = createMarker(ia);//using given healper method
	         to.setRadius(10.0f); 

	         Object magObj = ia.getProperty("magnitude");
	         float mag = Float.parseFloat(magObj.toString());
	         if(mag >= 5.0f ){

	              to.setColor(red);
	              }
	         else if(mag >= 4.0f ){

	              to.setColor(yellow);
	              }
	         else{

	              to.setColor(blue);
	              }

	          markers.add(to);//adding created simplepointmarkers to(to) arraylist


	     }//end of for loop

	   map.addMarkers(markers);//entire list of markers is  added to map
	    
	    
	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}
	// Here is an example of how to use Processing's color method to generate 
    // an int that represents the color yellow.  
    int yellow = color(255, 255, 0);
    int red = color(255, 0, 0);
    int blue = color(0, 0, 255); 
    
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		//System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		
		// Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
		
		// TODO (Step 4): Add code below to style the marker's size and color 
	    // according to the magnitude of the earthquake.  
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")
	    
	    
	    // Finally return the marker
	    // finish implementing and use this method, if it helps.
        return new SimplePointMarker(feature.getLocation());

	}
	
	public void draw() {
	    background(211, 222, 220);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
    {
        // Remember you can use Processing's graphics methods here
        fill(255, 250, 240); //color white
        rect(25, 50, 150, 250); // (x location of upper left corner, y location of upper left corner, width, height)

        fill(0); //needed for text to appear, sets the color to fill shapes, takes in an int rgb value
        textAlign(LEFT, CENTER);
        textSize(12);
        text("Earthquake Key", 50, 75); //heading of key, takes (string, float x, and float y)

        fill(color(255, 0, 0)); //red
        ellipse(50, 125, 15, 15); //(x coordinate, y coordinate, width, height)   )
        fill(color(255, 255, 0)); //yellow 
        ellipse(50, 175, 10, 10);
        fill(color(0, 0, 255));
        ellipse(50, 225, 5, 5);

        fill(0, 0, 0);
        text("5.0+ Magnitude", 75, 125);
        text("4.0+ Magnitude", 75, 175); // same y coordinate but different x so it could appear right beside marker
        text("Below 4.0", 75, 225);
    }
}

