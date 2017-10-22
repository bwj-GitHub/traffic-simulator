package events.carEvents;

public class CarUpdateEvent extends CarEvent {
	int carId;
	int intersectionRowIndex;
	int intersectionColIndex;

	public CarUpdateEvent(int carId, int intersectionRowIndex,
			int intersectionColIndex, float time) {
		super(time);
		this.carId = carId;
		this.intersectionRowIndex = intersectionRowIndex;
		this.intersectionColIndex = intersectionColIndex;
	}

}
