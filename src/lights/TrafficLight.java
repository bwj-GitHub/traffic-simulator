package lights;

import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;
import grid.Intersection;
import traffic.Car;
import traffic.TrafficQueue;

public class TrafficLight {
	public boolean isAvenueLight;
	public Intersection intersection;
	public LightColor color;
	private TrafficQueue[] trafficQueues;
	
	public TrafficLight(Intersection intersection, boolean isAvenueLight) {
		this.isAvenueLight = isAvenueLight;
		this.intersection = intersection;
		int maxCars;
		if (isAvenueLight) {
			maxCars = intersection.inAvenue.length;
		} else {
			maxCars = intersection.inStreet.length;
		}
		
		// Initialize trafficQueues (L, M, R):
		trafficQueues = new TrafficQueue[3];  // Assumes 3 lanes.
		for (int i = 0; i < trafficQueues.length; i++) {
			trafficQueues[i] = new TrafficQueue(maxCars, intersection);
		}
	}
	
	public String toString() {
		return String.format("TrafficLight(%s, %b)", intersection.toString(),
				isAvenueLight);
	}

	public boolean isGreen() {
		// TODO: Once acceleration is implemented, YELLOW should no longer count
		//  as GREEN.
		if (color == LightColor.GREEN || color == LightColor.YELLOW) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isQueueFull(int laneIndex) {
		// TODO: Check size of appropriate queue:
		return trafficQueues[1].isFull();
	}
	
	public void addCarToTrafficQueue(Car car) {
		// TODO: Determine lane and place car in appropriate TrafficQueue:
		trafficQueues[1].addCar(car);
		// TODO: Write me!
	}

	public CarEvent[] updateLight(LightEvent event) {
		this.color = event.color;
		if (event.color == LightColor.GREEN) {
			// TODO: update each TrafficQueue
			return trafficQueues[1].updateCars(event);
		} else {
			return null;
		}
	}
}
