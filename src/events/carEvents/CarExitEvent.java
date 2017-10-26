package events.carEvents;

import traffic.Car;

public class CarExitEvent extends CarEvent {
	public Car car;

	public CarExitEvent(Car car, float time) {
		super(time);
		this.car = car;
	}
	
	public String toString() {
		return String.format("CarExitEvent(%d %f)", car.id, time());
	}

}
