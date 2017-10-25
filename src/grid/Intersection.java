package grid;

import events.Event;
import events.lightEvents.LightEvent;
import lights.TrafficLight;
import traffic.Car;

public class Intersection {
	public int intersectionRowIndex;
	public int intersectionColIndex;

	TrafficLight avenueLight;
	TrafficLight streetLight;
	
	public RoadSegment inAvenue;
	public RoadSegment outAvenue;
	public RoadSegment inStreet;
	public RoadSegment outStreet;

	public Intersection(int row, int col, RoadSegment[] roadSegments) {
		this.intersectionRowIndex = row;
		this.intersectionColIndex = col;
		
		this.inAvenue = roadSegments[0];
		this.outAvenue = roadSegments[1];
		this.inStreet = roadSegments[2];
		this.outStreet = roadSegments[3];
		setRoadSegmentIntersections();

		this.streetLight = new TrafficLight();
		this.avenueLight = new TrafficLight();
	}
	
	/**
	 * Set this Intersection as the outIntersection for its in- Roads.
	 */
	private void setRoadSegmentIntersections() {
		this.inAvenue.outIntersection = this;
		this.inStreet.outIntersection = this;
	}

	public Event[] handleLightEvent(LightEvent event) {
		// TODO: Write me!
	}
	
	public TrafficLight getTrafficLight(boolean onAvenue) {
		if (onAvenue) {
			return this.avenueLight;
		} else {
			return this.streetLight;
		}
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
	
	public void addToTrafficQueue(Car car) {
		// TODO: Write me!
	}
}
