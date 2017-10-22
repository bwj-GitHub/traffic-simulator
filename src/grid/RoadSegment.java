package grid;

import events.carEvents.CarEvent;
import traffic.Car;

public class RoadSegment {
	int roadIndex;
	int segmentIndex;
	boolean isAvenue;
	float length;
	Lane[] lanes;
	Intersection outIntersection;

	public RoadSegment(int roadIndex, int segmentIndex, boolean isAvenue,
			float length, Lane[] lanes, Intersection outIntersection) {
		// TODO: Do we really need the inIntersection?
		this.roadIndex = roadIndex;
		this.segmentIndex = segmentIndex;
		this.length = length;
		this.lanes = lanes;
		this.outIntersection = outIntersection;
	}
	
	public CarEvent[] addCar(Car car) {
		// TODO: Write me!
		int laneIndex = car.getLaneIndex();
		// Find appropriate lane
		
	}

}
