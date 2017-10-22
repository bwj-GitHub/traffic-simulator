package grid;

import events.carEvents.CarEvent;
import traffic.Car;

public class Road {
	int index;
	boolean isAvenue;
	// NOTE: Segments are stored in NS or EW order (regardless of orientation of Road)
	private Intersection[] intersections;
	RoadSegment[] roadSegments;
	int firstRoadSegmentIndex;

	public Road(int index, boolean isAvenue, Intersection[] intersections) {
		this.index = index;
		this.isAvenue = isAvenue;
		this.intersections = intersections;
		this.initRoadSegments();

		// Calculate index of first RoadSegment
		if (index % 2 == 0) {
			// Even Roads: NS/EW?
			this.firstRoadSegmentIndex = 0;
		} else {
			// Odd Roads: SN/WE?
			this.firstRoadSegmentIndex = this.roadSegments.length;
		}
	}
	
	private void initRoadSegments() {
		// TODO: Write me!
	}

	public CarEvent[] addCar(Car car) {
		CarEvent[] nextCarEvents = null;
		nextCarEvents = roadSegments[this.firstRoadSegmentIndex].addCar(car);
		return nextCarEvents;
	}

}
