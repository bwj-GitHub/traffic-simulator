package grid;

import events.carEvents.CarEvent;
import events.carEvents.CarUpdateEvent;
import lights.TrafficLight;
import traffic.Car;
import traffic.TrafficQueue;

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
		if (index % 2 == 0) {
			// Even Roads: NS/EW?
			this.firstRoadSegmentIndex = 0;
		} else {
			// Odd Roads: SN/WE?
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
		// Determine car's next Intersection:
		RoadSegment roadSegment = roadSegments[firstRoadSegmentIndex];
		Intersection intersection = roadSegment.outIntersection;
		int i = intersection.intersectionRowIndex;
		int j = intersection.intersectionColIndex;

		// Check if this first RoadSegment is full:
		TrafficLight trafficLight = intersection.getTrafficLight(car.onAvenue);
		TrafficQueue trafficQueue = trafficLight.trafficQueue;
		if (trafficQueue.isFull()) {
			// Place the car in the TrafficQueue -- it is okay for the first segment
			//  to be over-full:
			trafficQueue.addCar(car);

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
		float nextTime = car.nextEvent.time() + travelTime;
		CarUpdateEvent nextEvent = new CarUpdateEvent(car.id, i, j, nextTime);

		// Update car's state:
		car.setSegmentIndex(firstRoadSegmentIndex);
		car.updateNextEvent(nextEvent);

		return nextEvent;
	}
}
