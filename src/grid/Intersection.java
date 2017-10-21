package grid;

import events.Event;
import events.lightEvents.LightEvent;
import lights.TrafficLight;

public class Intersection {
	int intersectionRowIndex;
	int intersectionColIndex;

	TrafficLight streetLight;
	TrafficLight avenueLight;
	
	public Intersection(int i, int j) {
		this.intersectionRowIndex = i;
		this.intersectionColIndex = j;
		this.streetLight = new TrafficLight();
		this.avenueLight = new TrafficLight();
	}
	
	public Event[] handleLightEvent(LightEvent event) {
		
	}

}
