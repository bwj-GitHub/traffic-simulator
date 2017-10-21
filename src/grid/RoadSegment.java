package grid;

import traffic.Car;

public class RoadSegment {
	int i;
	int j;
	float length;
	Lane[] lanes;
	Intersection inIntersection;
	Intersection outIntersection;

	public RoadSegment(int i, int j, float length, Lane[] lanes,
			Intersection inIntersection, Intersection outIntersection) {
		// TODO: Do we really need the inIntersection?
		this.i = i;
		this.j = j;
		this.length = length;
		this.lanes = lanes;
		this.inIntersection = inIntersection;
		this.outIntersection = outIntersection;
	}
	
	public CarEvent[] addCar(Car car) {
		// TODO: Write me!
		int laneIndex = car.getLaneIndex();
		// Find appropriate lane
		
	}

}
