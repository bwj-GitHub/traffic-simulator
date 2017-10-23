package events.carEvents;

public class CarExitEvent extends CarEvent {
	public int carId;

	public CarExitEvent(int carId, float time) {
		super(time);
		this.carId = carId;
	}

}
