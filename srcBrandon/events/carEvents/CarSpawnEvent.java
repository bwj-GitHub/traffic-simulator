package events.carEvents;

import cars.Car;

/**
 * An Event indicating that a car should be generated at `time` and placed
 * in the TrafficGrid.
 * @author brandon
 *
 */
public class CarSpawnEvent extends CarEvent {

	public CarSpawnEvent(float time) {
		super(time);
	}
	
	public String toString() {
		return "CarSpawnEvent(" + time() + "s)";
	}

}
