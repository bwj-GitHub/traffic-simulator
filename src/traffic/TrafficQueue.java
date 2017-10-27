package traffic;

import java.util.ArrayList;

import events.carEvents.CarEvent;
import events.carEvents.CarUpdateEvent;
import events.lightEvents.LightEvent;
import grid.Intersection;

public class TrafficQueue {

	int maxCars;  // this limit can be exceeded by entry points
	int laneIndex;
	private Intersection intersection;
	public ArrayList<Car> queue;


	public TrafficQueue(int maxCars, Intersection intersection, int laneIndex) {
		this.maxCars = maxCars;
		this.laneIndex = laneIndex;
		this.intersection = intersection;
		this.queue = new ArrayList<Car>();
	}

	public void addCar(Car car) {
		this.queue.add(car);
	}
	
	public void removeCar(int index) {
		Car car = queue.remove(index);
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

	public CarEvent[] updateCars(LightEvent event) {
		// Create Create CarUpdateEvents for each car
		// NOTE: This method assumes that light durations are unavailable
		int n = intersection.intersectionRowIndex;
		int m = intersection.intersectionColIndex;

		// Create CarUpdateEvents for each Car (for their CURRENT Intersection):
		CarUpdateEvent[] updateEvents = new CarUpdateEvent[queue.size()];
		int carsInQueue = queue.size();
		for (int i = 0; i < carsInQueue; i++) {
			Car car = queue.get(0);
			float distance = (float) i + intersection.getLength(car.onAvenue());
			float travelTime = car.timeToDistance(distance);
			float eventTime = travelTime + event.time();
			CarUpdateEvent nextEvent = new CarUpdateEvent(car.id, n, m, eventTime);
			car.updateNextEvent(nextEvent);
			updateEvents[i] = nextEvent;
			removeCar(0);
		}
		return updateEvents;
	}

}
