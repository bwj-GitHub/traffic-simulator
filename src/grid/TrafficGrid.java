package grid;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

import events.Event;
import events.EventHandler;
import events.carEvents.CarEvent;
import events.carEvents.CarSpawnEvent;
import events.carEvents.CarExitEvent;
import events.carEvents.CarUpdateEvent;
import simulator.Config;
import traffic.Car;
import traffic.CarFactory;
import traffic.Path;


// TODO: Update doc
/**
 * A 2-D array of Intersections representing a city?
 * 
 * @author brandon
 *
 */
public class TrafficGrid implements EventHandler{
	int n;  // num Streets
	int m;  // num Avenues
	Random random;
	CarFactory carFactory;
	Road[] avenues;  // NS or SN
	Road[] streets;
	Intersection[][] intersections;

	HashSet<Integer> carIds;
	HashMap<Integer, Car> cars;

	public TrafficGrid(Config config, Random random) {
		this.n = config.nRows;
		this.m = config.nCols;
		this.random = random;
		this.carFactory = new CarFactory(n, m, random);

		this.initIntersections();
		this.initRoads();
		this.carIds = new ArrayList<Integer>();
		this.cars = new HashMap<Integer, Car>();
	}

	private void initIntersections() {
		intersections = new Intersection[n][m];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				intersections[i][j] = new Intersection(i, j);
			}
		}
	}

	private void initRoads() {
		// Initialize Avenues:
		avenues = new Road[m];
		for (int i = 0; i < m; i++) {
			// Determine which Intersections belong on the Road
			Intersection[] roadIntersections = new Intersection[m];
			for (int j = 0; j < n; j++) {
				roadIntersections[j] = intersections[j][i];
			}
			Road newRoad = new Road(i, true, roadIntersections);
			avenues[i] = newRoad;
		}

		// Initialize Streets:
		streets = new Road[n];
		for (int i = 0; i < n; i++) {
			// Determine which Intersections belong on the Road
			Intersection[] roadIntersections = new Intersection[n];
			for (int j = 0; j < m; j++) {
				roadIntersections[j] = intersections[i][j];
			}
			Road newRoad = new Road(i, false, roadIntersections);
			streets[i] = newRoad;
		}
	}

	@Override
	public Event[] handleEvent(Event event) {
		Event[] nextEvents = null;
		// Determine type of Event:
		if (event instanceof CarSpawnEvent){
			// Handle CarSpawnEvent
			nextEvents = handleCarSpawnEvent((CarSpawnEvent) event);
		} else if (event instanceof CarUpdateEvent) {
			// Handle CarUpdateEvent
			CarEvent nextEvent = handleCarUpdateEvent(
					(CarUpdateEvent) event);
			nextEvents = new Event[] {nextEvent};
		} else if (event instanceof CarExitEvent) {
			// Handle CarExitEvent
			handleCarExitEvent((CarExitEvent) event);
		} 

		return nextEvents;
	}

	private CarEvent[] handleCarSpawnEvent(CarSpawnEvent event) {
		// Create a Car:
		Car newCar = this.carFactory.newCar(event.time());
		Path carPath = newCar.getPath();

		carIds.add(newCar.getId());
		cars.put(newCar.getId(), newCar);

		CarEvent[] newCarEvents = null;
		if (carPath.startAvenue) {
			// Place newCar in the appropriate Avenue
			newCarEvents = avenues[carPath.startIndex].addCar(newCar);
		} else {
			// Place newCar in the appropriate Street
			newCarEvents = streets[carPath.startIndex].addCar(newCar);
		}

		// Create next CarSpawnEvent:
		float nextArrivalTime = getNextArrivalTime(event.time());
		CarSpawnEvent nextSpawnEvent = new CarSpawnEvent(nextArrivalTime);

		// Return new Events:
		int numEvents = 1 + newCarEvents.length;
		CarEvent[] nextEvents = new CarEvent[numEvents];
		for (int i = 0; i < numEvents - 1; i++) {
			nextEvents[i] = newCarEvents[i];
		}
		nextEvents[numEvents - 1] = (CarEvent) nextSpawnEvent;
		return nextEvents;
	}

	private float getNextArrivalTime(float time) {
		// TODO: Calculate next arrival time
		return 0.0f;
	}

	private void handleCarExitEvent(CarExitEvent event) {
		// TODO: handle event
	}

	private CarEvent handleCarUpdateEvent(CarUpdateEvent event){
		// TODO: handle event
	}

}
