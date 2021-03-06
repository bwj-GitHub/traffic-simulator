package grid;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

import cars.Car;
import cars.CarFactory;
import cars.Path;
import events.Event;
import events.EventHandler;
import events.carEvents.CarEvent;
import events.carEvents.CarSpawnEvent;
import events.carEvents.CarExitEvent;
import events.carEvents.CarUpdateEvent;
import lights.TrafficLight;
import simulator.Config;
import simulator.InterarrivalTimeGenerator;


/**
 * Handles flow of cars through the modeled traffic grid.
 * 
 * @author brandon
 *
 */
public class TrafficGrid implements EventHandler{
	Config config;
	int n;  // num Streets
	int m;  // num Avenues
	Random random;
	InterarrivalTimeGenerator interArrival;
	CarFactory carFactory;
	int carSpawnLimit;
	int numCarsSpawned;
	Road[] avenues;  // NS or SN
	Road[] streets;  // EW or WE
	public Intersection[][] intersections;

	HashSet<Integer> carIds;
	public HashMap<Integer, Car> cars;

	public TrafficGrid(Config config, CarFactory carFactory,
			InterarrivalTimeGenerator interArrival) {
		this.config = config;
		this.n = config.nRows;
		this.m = config.nCols;
		this.interArrival = interArrival;
		this.carFactory = carFactory;
//		this.carFactory = new CarFactory(n, m, random);
		this.carSpawnLimit = config.carSpawnLimit;
		this.numCarsSpawned = 0;

		this.initRoads();
		this.initIntersections();

		this.carIds = new HashSet<Integer>();
		this.cars = new HashMap<Integer, Car>();
	}

	private void initRoads() {
		// Initialize Avenues:
		avenues = new Road[m];
		for (int i = 0; i < m; i++) {
			avenues[i] = new Road(i, true, config.dRows);
		}

		// Initialize Streets:
		streets = new Road[n];
		for (int i = 0; i < n; i++) {
			streets[i] = new Road(i, false, config.dCols);
		}
	}

	private void initIntersections() {
		intersections = new Intersection[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				RoadSegment[] roadSegments = getIntersectionRoadSegments(i, j);
				intersections[i][j] = new Intersection(i, j, roadSegments);
				
//				// FIXME: Debugging print:
//				System.out.println(String.format("Debugging Intersection %d, %d",
//						i, j));
//				for (RoadSegment r : roadSegments) {
//					System.out.println(r);
//				}
			}
		}
	}

	/**
	 * Return the list of RoadSegments that touch the indicated Intersection.
	 * @param i the row that the Intersection is in.
	 * @param j the column that the Intersection is in.
	 * @return an Intersection[]: {inAvenue, outAvenue, inStreet, outStreet}.
	 */
	private RoadSegment[] getIntersectionRoadSegments(int i, int j) {
		//....|....|....|....|....
		//--<-#--<-#--<-#--<-#----<
		//....|....|....|....|....
		//->--#->--#->--#->--#---->
		//    v    ^    v    ^
		// If intersection col is even: inc Av; else dec Av (for out)
		// If intersection row is even: dec St; else inc St (for out)
		RoadSegment inAvenue, outAvenue, inStreet, outStreet;
		// Determine In/Out Avenues:
		if (j % 2 == 0) {
			inAvenue = this.avenues[j].roadSegments[i];
			outAvenue = this.avenues[j].roadSegments[i+1];
		} else {
			inAvenue = this.avenues[j].roadSegments[i+1];
			outAvenue = this.avenues[j].roadSegments[i];
		}

		// Determine In/Out Streets:
		if (i % 2 == 0) {
			inStreet = this.streets[i].roadSegments[j+1];
			outStreet = this.streets[i].roadSegments[j];
		} else {
			inStreet = this.streets[i].roadSegments[j];
			outStreet = this.streets[i].roadSegments[j+1];
		}
		return new RoadSegment[] {inAvenue, outAvenue, inStreet, outStreet};
	}

	@Override
	public Event[] handleEvent(Event event) throws Exception {
		Event[] nextEvents = null;
		// Determine type of Event:
		if (event instanceof CarSpawnEvent){
			// Handle CarSpawnEvent
			nextEvents = handleCarSpawnEvent((CarSpawnEvent) event);
		} else if (event instanceof CarUpdateEvent) {
			// Handle CarUpdateEvent
			CarEvent nextEvent = handleCheckIntersectionEvent(
					(CarUpdateEvent) event);			
			nextEvents = new Event[] {nextEvent};
		} else if (event instanceof CarExitEvent) {
			// Handle CarExitEvent
			handleCarExitEvent((CarExitEvent) event);
		}
		return nextEvents;
	}

	private CarEvent[] handleCarSpawnEvent(CarSpawnEvent event) throws Exception {
		// Check if the car spawn limit has been reached:
		if (numCarsSpawned >= this.carSpawnLimit) {
			return null;
		} else {
			numCarsSpawned += 1;
		}
		
		// Create a Car:
		Car newCar = this.carFactory.newCar(event.time());
		newCar.updateNextEvent(event);
		Path carPath = newCar.getPath();

		// Keep track of Car in this:
		carIds.add(newCar.id);
		cars.put(newCar.id, newCar);

		// Generate the Car's next Events (CarUpdateEvent):
		CarEvent newCarEvent = null;
		if (carPath.startAvenue) {
			// Place newCar in the appropriate Avenue
			newCarEvent = avenues[carPath.startIndex].handleNewCar(newCar);
		} else {
			// Place newCar in the appropriate Street
			newCarEvent = streets[carPath.startIndex].handleNewCar(newCar);
		}

		// Create next CarSpawnEvent:
		float nextArrivalTime = getNextArrivalTime(event.time());
		CarSpawnEvent nextSpawnEvent = new CarSpawnEvent(nextArrivalTime);

		// Return new Events:
		return new CarEvent[] {newCarEvent, nextSpawnEvent};
	}

	private float getNextArrivalTime(float time) {
		return time + (float) interArrival.getNextArrivalTime();
	}

	private void handleCarExitEvent(CarExitEvent event) {
		// NOTE: It SHOULD be guaranteed that the car is no longer in
		//  ANY TrafficQueue, if this Event was produced
		// An Exception should be raised if the car still has turns remaining:
		if (event.car.getLaneIndex() != 1) {
			System.err.println("Car " + event.car + " still has turns remaining!");
//			throw new Exception();
		}
		carIds.remove(event.car.id);
		cars.remove(event.car.id);
	}

	/**
	 * Check the status of an intersection and create the next event
	 * accordingly.
	 * 
	 * If the light is green, a new CheckIntersectionEvent will be created
	 * for the next Intersection that the car will cross (or, a
	 * CarExitEvent, if there are no more intersections); otherwise, the car
	 * will be placed in the TrafficQueue for the light.
	 * 
	 * @param event
	 * @return
	 */
	private CarEvent handleCheckIntersectionEvent(CarUpdateEvent event) {
		// NOTE: this ignores any cars on the road (if acceleration and
		//  deceleration are instant)

		// Determine Car indicated by the event:
		int carId = event.carId;
		Car car = this.cars.get(carId);

		// Determine Intersection indicated by the event:
		int n = event.intersectionRowIndex;
		int m = event.intersectionColIndex;
		Intersection intersection = intersections[n][m];
		RoadSegment roadSegment = car.roadSegment;
		TrafficLight trafficLight = intersection.getTrafficLight(
				roadSegment.isAvenue);

		// Determine car's next Event, based on trafficLight's color:
		// TODO: Once acceleration is implemented, yellow should behave differently
		if ((car.onAvenue() && !intersection.avenueLight.isRed()) ||
				(!car.onAvenue() && !intersection.streetLight.isRed())) {
			// Light is green, check if the intersection can be crossed:
			RoadSegment nextRoadSegment = car.getNextRoadSegment();
			Intersection nextIntersection = nextRoadSegment.outIntersection;

			// Check if the nextRoadSegment is an Exit point:
			if (nextIntersection == null) {
				// Car should exit:
				return car.crossIntersection();
			}

			TrafficLight nextTrafficLight = nextIntersection.getTrafficLight(
					nextRoadSegment.isAvenue);

			int laneIndex = car.getLaneIndex();
			if (!nextTrafficLight.isQueueFull(laneIndex)) {
				// Space is available, cross Intersection:
				
				CarEvent nextEvent = car.crossIntersection();
				return nextEvent;
			} else {
				// No room is available on the next RoadSegment (or there
				//  are still cars on this RoadSegment),
				//  place car in TrafficQueue
				nextTrafficLight.addCarToTrafficQueue(car);
				car.updateNextEvent(null);
				return null;
			}

		} else {
			// Light is red, place car in the TrafficQueue
			car.updateNextEvent(null);  // Car has no nextEvent
			trafficLight.addCarToTrafficQueue(car);
			return null;
		}		
	}
}
