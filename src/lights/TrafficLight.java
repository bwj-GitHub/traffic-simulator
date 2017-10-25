package lights;

import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;
import traffic.TrafficQueue;

public class TrafficLight {
	public LightColor color;
	public float minTimeToNextChange; 
	public TrafficQueue trafficQueue;

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
