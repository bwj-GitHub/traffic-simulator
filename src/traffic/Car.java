package traffic;

import events.Event;

public class Car {

	int id;
	float enterTime;
	float exitTime;
	float velocity;

	// Location
	Path path;
	int pathIndex;  // Indicates how many segments on its Path it has traveled
	int laneIndex;  // 0 is Left, 1 is Middle, 2 is Right
	float position;  // Distance traveled in current road segment

	Event lastEvent;  // last Event that was handled
	Event nextEvent;  // next Event to be handled

	public Car(int id, float enterTime, Path path) {
		this.id = id;
		this.enterTime = enterTime;
		this.exitTime = -1.0f;  // has not exited yet
		this.velocity = 0;  // TODO: or maxVelocity?

		this.path = path;
		this.pathIndex = 0;
		this.laneIndex = path.getLane(pathIndex);
		this.position = 0;

		this.lastEvent = null;
		this.nextEvent = null;
	}

	// Getters:

	public int getId() {
		return this.id;
	}
	
	public float getEnterTime() {
		return this.enterTime;
	}
	
	public float getExitTime() {
		return this.exitTime;
	}
	
	public float getVelocity() {
		return this.velocity;
	}

	public Path getPath() {
		return this.path;
	}

	public int getPathIndex() {
		return this.pathIndex;
	}

	public int getLaneIndex() {
		return this.laneIndex;
	}

	public float getPosition() {
		return this.position;
	}

	public Event getLastEvent() {
		return this.lastEvent;
	}

	public Event getNextEvent() {
		return this.nextEvent;
	}

	// Setters:

	public void setExitTime(float exitTime) {
		this.exitTime = exitTime;
	}
	
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public void setPathIndex(int pathIndex) {
		this.pathIndex = pathIndex;
	}

	public void setLaneIndex(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	public void setPosition(float position) {
		this.position = position;
	}

	// TODO: Is this necessary? Just update when setting nextEvent.
	public void setLastEvent(Event lastEvent) {
		this.lastEvent = lastEvent;
	}

	public void setNextEvent(Event nextEvent) {
		this.nextEvent = nextEvent;
	}
	
	// Update methods:
	// These should generally be used instead of setters:
	
	public void incrementPathIndex() {
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
	public float timeToDistance(float position, float acceleration) {
		float distanceToTravel = position - this.distance;
		// TODO: MATHS
		return 0.0f;
	}

}
