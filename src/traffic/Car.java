package traffic;

import events.Event;

public class Car {

	public int id;
	public float enterTime;
	public float exitTime;
	public float velocity;   // NOTE: not currently being used as we are ignoring acc.
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

	public Car(int id, float enterTime, Path path, int segmentIndex) {
		this.id = id;
		this.enterTime = enterTime;
		this.exitTime = -1.0f;  // has not exited yet
		this.velocity = 0;  // TODO: or maxVelocity?
		this.lastEvent = null;  // a CarSpawnEvent?
		this.nextEvent = null;

		this.onAvenue = path.startAvenue;
		this.roadIndex = path.startIndex;
		this.segmentIndex = segmentIndex;
		this.laneIndex = path.getLaneIndex(pathIndex);

		this.path = path;
		this.pathIndex = 0;
	}

	// Getters:

	public Path getPath() {
		return this.path;
	}
	
	/** 
	 * Return true if the car is turning at its next Intersection.
	 * @return boolean.
	 */
	public boolean isTurning() {
		// TODO: Write me!
	}

	// Update methods:

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
	public float timeToDistance(float distance, float acceleration) {
		// TODO: MATHS
		return 0.0f;
	}

}
