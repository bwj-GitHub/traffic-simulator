package events.carEvents;

import cars.Car;

public class CarExitEvent extends CarEvent {
	public Car car;

	public CarExitEvent(Car car, float time) {
		super(time);
		this.car = car;
	}
	
	public String toString() {
		float timeInGrid = time() - car.enterTime;
		return String.format("CarExitEvent   (carId=%3d, t=%.2fs, time-in-grid=%fs)",
				car.id, time(), timeInGrid);
	}

}
