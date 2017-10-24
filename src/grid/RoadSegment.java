package grid;

import events.carEvents.CarEvent;
import traffic.Car;

public class RoadSegment {
	int roadIndex;
	int segmentIndex;
	boolean isAvenue;
	float length;
	Intersection outIntersection;

	public RoadSegment(int roadIndex, int segmentIndex, boolean isAvenue,
			float length, Intersection outIntersection) {
		this.roadIndex = roadIndex;
		this.segmentIndex = segmentIndex;
		this.length = length;
		this.outIntersection = outIntersection;
		this.initLanes();
	}
	
	private void initLanes() {
		// TODO: Write me!
	}
	
	public CarEvent[] addCar(Car car) {
		// Find appropriate lane
		CarEvent[] nextCarEvents = null;
		nextCarEvents = lanes[car.getLaneIndex()].addCar(car);
		return nextCarEvents;
	}

}
