package traffic;

import events.Event;

public class Car {

	public int id;
	public float enterTime;
	public float exitTime;
	public float velocity;   // NOTE: not currently being used as we are ignoring acc.
	public float acceleration;  // NOTE: ""
	public Event lastEvent;  // last Event that was handled
	public Event nextEvent;  // next Event to be handled

	// Location
	public boolean onAvenue;
	public int roadIndex;
	public int segmentIndex;
	public int laneIndex;  // 0 is Left, 1 is Middle, 2 is Right

	// Progress towards exit:
	Path path;
	public int pathIndex;  // Indicates next turn

	public Car(int id, float enterTime, Path path) {
		this.id = id;
		this.enterTime = enterTime;
		this.exitTime = -1.0f;  // has not exited yet
		this.velocity = 0;  // TODO: or maxVelocity?
		this.acceleration = 1;  // TODO: Read from Config
		this.lastEvent = null;  // a CarSpawnEvent?
		this.nextEvent = null;

		this.onAvenue = path.startAvenue;
		this.roadIndex = path.startIndex;
		this.segmentIndex = -1;  // to be set latter
		this.laneIndex = path.getLaneIndex(pathIndex);

		this.path = path;
		this.pathIndex = 0;
	}

	public Path getPath() {
		return this.path;
	}

	public void setSegmentIndex(int segmentIndex) {
		this.segmentIndex = segmentIndex;
	}
	
	/** 
	 * Return true if the car is turning at its next Intersection.
	 * @return boolean.
	 */
	public boolean isTurning() {
		if (laneIndex == 1) {
			return false;
		} else {
			return true;
		}
	}

	public void incrementPathIndex() {
		// Should only be called after the car completes a turn
		this.pathIndex += 1;
	}

	public void updateNextEvent(Event nextEvent) {
		this.lastEvent = this.nextEvent;
		this.nextEvent = nextEvent;
	}

	// Mathematical Calculations:

	/**
	 * Calculate the amount of time required to reach position in lane.
	 * @param position
	 * @param acceleration
	 */
	public float timeToDistance(float distance) {
		// TODO: MATHS
		return 0.0f;
	}

}
