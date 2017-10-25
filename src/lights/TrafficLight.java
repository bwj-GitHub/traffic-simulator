package lights;

import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;
import grid.Intersection;
import traffic.TrafficQueue;

public class TrafficLight {
	public boolean isAvenueLight;
	public LightColor color;
	public TrafficQueue trafficQueue;
	public Intersection intersection;
	
	public TrafficLight(Intersection intersection, boolean isAvenueLight) {
		this.isAvenueLight = isAvenueLight;
		this.intersection = intersection;
		int maxCars;
		if (isAvenueLight) {
			maxCars = intersection.inAvenue.length;
		} else {
			maxCars = intersection.inStreet.length;
		}
		this.trafficQueue = new TrafficQueue(maxCars, intersection);
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

	public CarEvent[] updateLight(LightEvent event) {
		if (event.color == LightColor.GREEN) {
			return trafficQueue.updateCars(event);
		} else {
			return null;
		}
	}
}
