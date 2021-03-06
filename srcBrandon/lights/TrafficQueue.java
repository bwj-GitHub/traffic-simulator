package lights;

import java.util.ArrayList;

import cars.Car;
import events.carEvents.CarEvent;
import events.carEvents.CarUpdateEvent;
import events.lightEvents.LightEvent;
import grid.Intersection;

public class TrafficQueue {

	int maxCars;  // this limit can be exceeded by entry points
	int laneIndex;
	private Intersection intersection;
	public float lastExitTime;  // the time the last Car in the queue crossed Intersection.
	public ArrayList<Car> queue;


	public TrafficQueue(int maxCars, Intersection intersection, int laneIndex) {
		this.maxCars = maxCars;
		this.laneIndex = laneIndex;
		this.intersection = intersection;
		this.lastExitTime = 0.0f;
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
//		if (carsInQueue > 5) {
//			System.out.println(carsInQueue);
//		}
		for (int i = 0; i < carsInQueue; i++) {
			Car car = queue.get(0);
			float distance = (float) i;  // car is i units from intersection
			float travelTime = car.timeToDistance(distance);
			float eventTime = travelTime + event.time();
			CarUpdateEvent nextEvent = new CarUpdateEvent(car.id, n, m, eventTime);
			car.updateNextEvent(nextEvent);
			updateEvents[i] = nextEvent;
			removeCar(0);

			// Update lastExitTime
			if (eventTime > lastExitTime) {
				lastExitTime = eventTime;
			}
		}
		return updateEvents;
	}

}
