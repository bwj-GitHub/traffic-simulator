package events.carEvents;

public class CheckIntersectionEvent extends CarEvent {
	int carId;
	int intersectionRowIndex;
	int intersectionColIndex;

	public CheckIntersectionEvent(int carId, int intersectionRowIndex,
			int intersectionColIndex, float time) {
		super(time);
		this.carId = carId;
		this.intersectionRowIndex = intersectionRowIndex;
		this.intersectionColIndex = intersectionColIndex;
	}

}
