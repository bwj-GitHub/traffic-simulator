package grid;

import events.Event;
import events.lightEvents.LightEvent;
import lights.TrafficLight;

public class Intersection {
	int intersectionRowIndex;
	int intersectionColIndex;

	TrafficLight avenueLight;
	TrafficLight streetLight;
	
	RoadSegment inAvenue;
	RoadSegment outAvenue;
	RoadSegment inStreet;
	RoadSegment outStreet;

	public Intersection(int row, int col, RoadSegment[] roadSegments) {
		this.intersectionRowIndex = row;
		this.intersectionColIndex = col;
		this.inAvenue = roadSegments[0];
		this.inAvenue = roadSegments[1];
		this.inAvenue = roadSegments[2];
		this.inAvenue = roadSegments[3];

		this.streetLight = new TrafficLight();
		this.avenueLight = new TrafficLight();
	}

	public Event[] handleLightEvent(LightEvent event) {
		// TODO: Write me!
	}
	
	/**
	 * 
	 * @param onAvenue indicates that the car is currently on an Avenue.
	 * @param turn indicates that the car should turn at the intersection.
	 * @return the RoadSegment that the car will be on after crossing
	 * 	this Intersection.
	 */
	public RoadSegment nextSegment(boolean onAvenue, boolean turn) {
		if (onAvenue) {
			if (turn) {
				return outStreet;
			}
			return outAvenue;
		} else {
			if (turn) {
				return outAvenue;
			}
			return outStreet;
		}
	}
}
