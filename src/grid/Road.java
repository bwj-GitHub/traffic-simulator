package grid;

import events.Event;
import events.carEvents.CarEvent;
import traffic.Car;

public class Road {
	int index;
	boolean isAvenue;
	RoadSegment[] roadSegments;
	int firstRoadSegmentIndex;

	public Road(int index, boolean isAvenue, RoadSegment[] roadSegments) {
		this.index = index;
		this.isAvenue = isAvenue;
		this.roadSegments = roadSegments;
		if (index % 2 == 0) {
			// Even Roads: NS/WE?
			this.firstRoadSegmentIndex = 0;
		} else {
			// Odd Roads: SN/EW?
			this.firstRoadSegmentIndex = this.roadSegments.length;
		}
	}

	public CarEvent[] addCar(Car car) {
		CarEvent[] nextCarEvents = null;
		nextCarEvents = roadSegments[this.firstRoadSegmentIndex].addCar(car);
		return nextCarEvents;
	}

}
