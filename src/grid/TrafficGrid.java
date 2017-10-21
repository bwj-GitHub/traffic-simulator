package grid;

import java.util.Random;

import events.Event;
import events.EventHandler;
import events.carEvents.CarEvent;
import events.carEvents.CarSpawnEvent;
import events.carEvents.CarEnterEvent;
import events.carEvents.CarExitEvent;
import events.carEvents.CheckIntersectionEvent;
import simulator.Config;
import traffic.Car;
import traffic.CarFactory;
import traffic.Path;


/**
 * A 2-D array of Intersections representing a city?
 * 
 * @author brandon
 *
 */
public class TrafficGrid implements EventHandler{

	int n;
	int m;
	Random random;
	CarFactory carFactory;
	Road[] avenues;
	Road[] streets;
	Intersection[][] intersections;

	// TODO: Init roads and intersections
	public TrafficGrid(int n, int m, Random random) {
		this.n = n;
		this.m = m;
		this.random = random;
		this.carFactory = new CarFactory(n, m, random);
	}

	// TODO: Call other constructor!
	public TrafficGrid(Config config, Random random) {
		this.n = config.nRows;
		this.m = config.nCols;
		this.random = random;
		this.carFactory = new CarFactory(n, m, random);
	}

	@Override
	public Event[] handleEvent(Event event) {
		Event[] nextEvents = null;
		// Determine type of Event:
		if (event instanceof CarSpawnEvent){
			// Handle CarSpawnEvent
			nextEvents = handleCarSpawnEvent((CarSpawnEvent) event);
		} else if (event instanceof CheckIntersectionEvent) {
			// Handle CheckIntersectionEvent
			CarEvent nextEvent = handleCheckIntersectionEvent(
					(CheckIntersectionEvent) event);
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

	private CarEvent handleCheckIntersectionEvent(CheckIntersectionEvent event){
		// TODO: handle event
	}

}
