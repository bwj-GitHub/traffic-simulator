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

	private float lengthPerLane;

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
	
		this.lengthPerLane = 1;  // TODO: Should be function of number of lanes
	}

	/**
	 * Set this Intersection as the outIntersection for its in- Roads.
	 */
	private void setRoadSegmentIntersections() {
		this.inAvenue.outIntersection = this;
		this.inStreet.outIntersection = this;
	}

	public Event[] handleLightEvent(LightEvent event) {
		// When a light changes color, we need to alert their respective trafficQueues
		TrafficLight light = event.light;
		return light.updateLight(event);
	}

	public float getLength(boolean avenue) {
		// TODO: should allow for variable number of lanes
		return 3 * lengthPerLane;
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
