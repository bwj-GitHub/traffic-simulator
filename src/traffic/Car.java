package traffic;

import events.Event;

public class Car {

	int id;
	// TODO: What is the Car's position?
	float enterTime;
	Path path;
	Event nextEvent;

	float exitTime;
	float velocity;
	// TODO: What am I doing about acceleration -- all cars have the same acc.
	//  A singleton?
	float distance;  // Distance traveled in current road segment

	public Car(int id, float enterTime, Path path) {
		this.id = id;
		this.enterTime = enterTime;
		this.path = path;

		this.nextEvent = null;
		this.exitTime = -1.0f;  // has not exited yet
		this.velocity = 0;
		this.distance = 0;
	}
	
	public int getLaneIndex() {
		// TODO: Return the index of the lane that the car should be in
	}
	
	public Path getPath() {
		return this.path;
	}

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
