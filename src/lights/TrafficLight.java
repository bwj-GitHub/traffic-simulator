package lights;

import java.util.ArrayList;

import cars.Car;
import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;
import grid.Intersection;

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
			trafficQueues[i] = new TrafficQueue(maxCars, intersection, i);
		}
	}

	public String toString() {
		return String.format("TrafficLight(%s, %b)", intersection.toString(),
				isAvenueLight);
	}
	
	public int getTotalQueueSize() {
		int totalQueueSize = 0;
		for (TrafficQueue queue: this.trafficQueues) {
			totalQueueSize += queue.queue.size();
		}
		return totalQueueSize;
	}

	public boolean isGreen() {
		// TODO: Once acceleration is implemented, YELLOW should no longer count
		//  as GREEN.
		if (color == LightColor.GREEN) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isRed() {
		if (color == LightColor.RED) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isYellow() {
		if (color == LightColor.YELLOW) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isQueueFull(int laneIndex) {
		return trafficQueues[laneIndex].isFull();
	}

	public void addCarToTrafficQueue(Car car) {
		trafficQueues[car.getLaneIndex()].addCar(car);
	}

	public CarEvent[] updateLight(LightEvent event, LightColor color) {
		ArrayList<CarEvent> carEvents = new ArrayList<CarEvent>();
		this.color = color;
		if (color == LightColor.GREEN) {
			for (int i = 0; i < trafficQueues.length; i++) {
				CarEvent[] queueEvents = trafficQueues[i].updateCars(event);
				for (CarEvent ce: queueEvents) {
					carEvents.add(ce);
				}
			}
			return carEvents.toArray(new CarEvent[carEvents.size()]);
		} else {
			return null;
		}
	}
}
