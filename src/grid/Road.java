package grid;

import events.carEvents.CarEvent;
import events.carEvents.CarUpdateEvent;
import lights.TrafficLight;
import traffic.Car;

public class Road {
	int index;
	boolean isAvenue;
	RoadSegment[] roadSegments;
	int firstRoadSegmentIndex;

	public Road(int index, boolean isAvenue, int[] segmentLengths) {
		this.index = index;
		this.isAvenue = isAvenue;
		this.initRoadSegments(segmentLengths);

		// Calculate index of first RoadSegment
		if ((isAvenue && index % 2 == 0) || (!isAvenue && index % 2 == 1)) {
			// Even Avenues/ Odd Streets
			this.firstRoadSegmentIndex = 0;
		} else {
			// Odd Avenues / Even Streets
			this.firstRoadSegmentIndex = this.roadSegments.length - 1;
		}
	}

	private void initRoadSegments(int[] segmentLengths) {
		int numSegments = segmentLengths.length;
		roadSegments = new RoadSegment[numSegments];
		for (int i = 0; i < numSegments; i++) {
			roadSegments[i] = new RoadSegment(index, i, segmentLengths[i],
					isAvenue);
		}
	}

	/**
	 * Update car's state to reflect its location at the beginning of this Road;
	 * return the car's next Event(s).
	 * 
	 * @param car
	 * @return CarEvent[]
	 */
	public CarEvent handleNewCar(Car car) {
		// Set car's RoadSegment:
		RoadSegment roadSegment = roadSegments[firstRoadSegmentIndex];
		car.roadSegment = roadSegment;

		// Determine car's next Intersection:
		Intersection intersection = roadSegment.outIntersection;
		System.out.println(roadSegment);  // FIXME: debugging!
		int i = intersection.intersectionRowIndex;
		int j = intersection.intersectionColIndex;

		// Check if this first RoadSegment is full:
		TrafficLight trafficLight = intersection.getTrafficLight(car.onAvenue());
		// TODO: Determine car's lane index
		if (trafficLight.isQueueFull(1)) {
			// Place the car in the TrafficQueue -- it is okay for the first segment
			//  to be over-full:
			trafficLight.addCarToTrafficQueue(car);

			// Update car's state:
			car.updateNextEvent(null);
			return null;
		}

		// Calculate time to reach next Intersection (assuming there is no cars in
		//  the TrafficQueue -- that will be handled later)
		// Calculate the time that the next event will occur:
		float travelDistance = roadSegment.length;
		// TODO: Road needs config?
		float travelTime = car.timeToDistance(travelDistance);
		System.out.println("travelDistance=" + travelDistance);
		System.out.println("travelTime=" + travelTime);
		float nextTime = car.nextEvent.time() + travelTime;
		CarUpdateEvent nextEvent = new CarUpdateEvent(car.id, i, j, nextTime);

		// Update car's nextEvent:
		car.updateNextEvent(nextEvent);

		return nextEvent;
	}
}
