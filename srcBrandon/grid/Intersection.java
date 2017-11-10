package grid;

import cars.Car;
import events.Event;
import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;
import lights.LightColor;
import lights.TrafficLight;

public class Intersection {
	public int intersectionRowIndex;
	public int intersectionColIndex;

	TrafficLight avenueLight;
	TrafficLight streetLight;

	public RoadSegment inAvenue;
	public RoadSegment outAvenue;
	public RoadSegment inStreet;
	public RoadSegment outStreet;

	public int numGreenExtensions;
	private float lengthPerLane;

	public Intersection(int row, int col, RoadSegment[] roadSegments) {
		this.intersectionRowIndex = row;
		this.intersectionColIndex = col;
		
		this.inAvenue = roadSegments[0];
		this.outAvenue = roadSegments[1];
		this.inStreet = roadSegments[2];
		this.outStreet = roadSegments[3];
		setRoadSegmentIntersections();

		this.avenueLight = new TrafficLight(this, true);
		this.streetLight = new TrafficLight(this, false);

		this.numGreenExtensions = 0;
		this.lengthPerLane = 1;  // TODO: Should be function of number of lanes
	}
	
	public String toString() {
		return String.format("Intersection(%d, %d)", intersectionRowIndex,
				intersectionColIndex);
	}

	/**
	 * Set this Intersection as the outIntersection for its in- Roads.
	 */
	private void setRoadSegmentIntersections() {
		this.inAvenue.outIntersection = this;
		this.inStreet.outIntersection = this;
	}

	public CarEvent[] handleLightEvent(LightEvent event) {
		// Determine the color of each light:
		TrafficLight greenYellowLight;  // green or yellow
		TrafficLight redLight;
		if (avenueLight.isRed()) {
			redLight = avenueLight;
			greenYellowLight = streetLight;
		} else {
			redLight = streetLight;
			greenYellowLight = avenueLight;
		}
		// Change light colors and update TrafficQueues
		if (greenYellowLight.isGreen()) {
			// Change GREEN light to YELLOW
			greenYellowLight.updateLight(event, LightColor.YELLOW);
			return null;
		} else {
			// Change YELLOW light to RED, and RED to GREEN
			greenYellowLight.updateLight(event, LightColor.RED);
			return redLight.updateLight(event, LightColor.GREEN);
		}
	}

	public float getLength(boolean avenue) {
		// TODO: should allow for variable number of lanes
		return 3 * lengthPerLane;
	}
	
	/**
	 * Return the TrafficLight that is currently GREEN.
	 * @return the TrafficLight that is currently GREEN; or, null if a light is
	 * currently YELLOW.
	 */
	public TrafficLight getGreenLight() {
		if (avenueLight.isGreen()) {
			return avenueLight;
		} else if (streetLight.isGreen()) {
			return streetLight;
		} else {
			return null;
		}
	}

	/**
	 * Return the TrafficLight that is currently RED.
	 * @return the TrafficLight that is currently RED.
	 */
	public TrafficLight getRedLight() {
		if (avenueLight.isRed()) {
			return avenueLight;
		} else {
			return streetLight;
		}
	}

	/**
	 * Return this intersection's avenueLight or streetLight.
	 * @param onAvenue: indicates that the avenueLight should be returned, if true;
	 *  otherwise, the streetLight.
	 * @return a TrafficLight.
	 */
	public TrafficLight getTrafficLight(boolean onAvenue) {
		if (onAvenue) {
			return this.avenueLight;
		} else {
			return this.streetLight;
		}
	}

	/**
	 * Return true if one of this Intersections TrafficLights is Yellow
	 * @return
	 */
	public boolean hasYellowLight() {
		if (avenueLight.isYellow() || streetLight.isYellow()) {
			return true;
		} else {
			return false;
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
