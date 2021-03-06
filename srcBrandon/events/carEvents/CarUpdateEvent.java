package events.carEvents;

public class CarUpdateEvent extends CarEvent {
	public int carId;
	public int intersectionRowIndex;
	public int intersectionColIndex;

	public CarUpdateEvent(int carId, int intersectionRowIndex,
			int intersectionColIndex, float time) {
		super(time);
		this.carId = carId;
		this.intersectionRowIndex = intersectionRowIndex;
		this.intersectionColIndex = intersectionColIndex;
	}

	public String toString() {
		return String.format("CarUpdateEvent (carId=%3d, t=%fs, intersections[%d][%d])",
				carId, time(), intersectionRowIndex, intersectionColIndex);
	}

}
