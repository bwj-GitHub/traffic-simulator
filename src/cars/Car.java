package cars;

import events.Event;
import events.carEvents.CarEvent;
import events.carEvents.CarExitEvent;
import events.carEvents.CarUpdateEvent;
import grid.Intersection;
import grid.RoadSegment;

public class Car {

	public int id;
	public float enterTime;
	public float exitTime;
	public float velocity;   // NOTE: not currently being used as we are ignoring acc.
	public float acceleration;  // NOTE: ""
	public Event lastEvent;  // last Event that was handled
	public Event nextEvent;  // next Event to be handled

	// Location
	public RoadSegment roadSegment;

	// Progress towards exit:
	public Path path;
	public int pathIndex;  // Indicates next turn

	public Car(int id, float enterTime, Path path) {
		this.id = id;
		this.enterTime = enterTime;
		this.exitTime = -1.0f;  // has not exited yet
		this.velocity = 5;  // TODO: or maxVelocity?
		this.acceleration = 1;  // TODO: Read from Config
		this.lastEvent = null;  // a CarSpawnEvent?
		this.nextEvent = null;

		this.roadSegment = null;

		this.path = path;
		this.pathIndex = 0;
	}
	
	public String toString() {
		// TODO: Clean up duplicate code
		if (getNextRoadSegment() == null) {
			return String.format("CAR.%d(pi=%d,onA=%b,RS=%s)", id, pathIndex,
					onAvenue(), roadSegment.toString());
		} else {
			return String.format("CAR.%d(pi=%d,onA=%b,RS=%s,NRS=%s)", id, pathIndex,
					onAvenue(), roadSegment.toString(), getNextRoadSegment().toString());
		}

	}
	
	public int getLaneIndex() {
		return path.getLaneIndex(pathIndex);
	}

	public Path getPath() {
		return this.path;
	}

	/**
	 * Return the RoadSegment that the car will be on after crossing its next
	 * intersection.
	 * @return a RoadSegment.
	 */
	public RoadSegment getNextRoadSegment() {
		// Determine the next RoadSegment and Intersection
		Intersection intersection = roadSegment.outIntersection;
		if (intersection == null) {
			return null;
		}
		RoadSegment nextRoadSegment;
		if ((onAvenue() && isTurning())
				|| (!onAvenue() && !isTurning())) {
			nextRoadSegment = intersection.outStreet;
		} else {
			nextRoadSegment = intersection.outAvenue;
		}
		return nextRoadSegment;
	}

	/** 
	 * Return true if the car is turning at its next Intersection.
	 * @return boolean.
	 */
	public boolean isTurning() {
		if (path.turns.length == 0 || pathIndex >= path.turns.length) {
			// Car's Path has no turns (or no turns remaining
			return false;
		}

		boolean onAvenue = path.onAvenue(pathIndex);
		Intersection intersection = roadSegment.outIntersection;
		
		if (intersection == null) {
			// This would only occur if the car is on its last RoadSegment
			return false;
		}
		
		int outRoadIndex;
		if (onAvenue) {
			outRoadIndex = intersection.outStreet.roadIndex;
		} else {
			outRoadIndex = intersection.outStreet.roadIndex;
		}

		if (outRoadIndex == path.turns[pathIndex]) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean onAvenue() {
		return this.roadSegment.isAvenue;
	}

	public void updateState(Event nextEvent, RoadSegment roadSegment) {
		// Determine if the Car (should have) turned:
		if (isTurning()) {
			incrementPathIndex();
		}

		// Update nextEvent and roadSegment:
		updateNextEvent(nextEvent);
		updateRoadSegment(roadSegment);
	}

	public void updateNextEvent(Event nextEvent) {
		if (this.nextEvent != null) {
			this.lastEvent = this.nextEvent;
		}
		this.nextEvent = nextEvent;
	}

	private void incrementPathIndex() {
		// Should only be called after the car completes a turn
		this.pathIndex += 1;
	}

	private void updateRoadSegment(RoadSegment roadSegment) {
		this.roadSegment = roadSegment;
	}


	/**
	 * Update car's state location to the RoadSegment it will arrive at
	 * after crossing intersection; returns the car's next CarEvent.
	 * 
	 * @return either a CarExitEvent or a CheckIntersectionEvent.
	 */
	public CarEvent crossIntersection() {
		// NOTE: Assumes that there is room available in the next RoadSegment
		// Need to create Event and update the Car's state (?)
		// Find the next Intersection or exit point:
		// To find the Intersection, we need to know if the car is turning
		Intersection intersection = roadSegment.outIntersection;

		// Determine the next RoadSegment and Intersection
		RoadSegment nextRoadSegment = getNextRoadSegment();
		Intersection nextIntersection = nextRoadSegment.outIntersection;

		// Calculate the time that the next event will occur:
		float travelDistance = nextRoadSegment.length + intersection.getLength(onAvenue());
		float travelTime = timeToDistance(travelDistance);
		// Note: nextEvent hasn't been updated yet
		float nextTime = nextEvent.time() + travelTime;

		// Create next Event:
		CarEvent nextEvent;
		if (nextIntersection == null) {
			// The car is exiting:
			nextEvent = new CarExitEvent(this, nextTime);
		} else {
			// Create the next CarUpdateEvent:
			int i = nextIntersection.intersectionRowIndex;
			int j = nextIntersection.intersectionColIndex;
			nextEvent = new CarUpdateEvent(id, i, j, nextTime);
		}

		// Update car's location:
		updateState(nextEvent, nextRoadSegment);

		return nextEvent;
	}

	// Mathematical Calculations:

	/**
	 * Calculate the amount of time required to travel distance.
	 * @param distance: the distance in unit length to be traveled.
	 */
	public float timeToDistance(float distance) {
		// TODO: Incorporate acceleration into calculation
		return distance / velocity;
	}

}
