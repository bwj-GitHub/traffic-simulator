package traffic;

import java.util.ArrayList;

import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;

public class TrafficQueue {

	int maxCars;  // this limit can be exceeded by entry points
	ArrayList<Car> queue;

	public TrafficQueue(int maxCars) {
		this.maxCars = maxCars;
		this.queue = new ArrayList<Car>();
	}

	public void addCar(Car car) {
		this.queue.add(car);
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	public boolean isFull() {
		if (queue.size() >= maxCars) {
			return true;
		} else {
			return false;
		}
	}

	// FIXME: Why not just handle this in TrafficGrid?
	public CarEvent[] updateCars(LightEvent event) {
		// Create Create CarUpdateEvents for each car
		// NOTE: This method assumes that light durations are unavailable
		// TODO: Write me!
	}

}
