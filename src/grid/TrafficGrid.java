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
import traffic.CarFactory;


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

	public TrafficGrid(int n, int m, Random random) {
		this.n = n;
		this.m = m;
		this.random = random;
		this.carFactory = new CarFactory(n, m, random);
	}

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
			// FIXME: this should spawn 2 events
			CarSpawnEvent nextCar = handleCarSpawnEvent((CarSpawnEvent) event);
			nextEvents = new Event[] {nextCar};
		} else if (event instanceof CarEnterEvent) {
			// Handle CarEnterEvent
			CheckIntersectionEvent nextEvent = handleCarEnterEvent(
					(CarEnterEvent) event);
			nextEvents = new Event[] {nextEvent};
		} else if (event instanceof CarExitEvent) {
			// Handle CarExitEvent
			handleCarExitEvent((CarExitEvent) event);
		} else if (event instanceof CheckIntersectionEvent) {
			// Handle CheckIntersectionEvent
			CarEvent nextEvent = handleCheckIntersectionEvent(
					(CheckIntersectionEvent) event);
			nextEvents = new Event[] {nextEvent};
		}
		return nextEvents;
	}

	private CarEvent[] handleCarSpawnEvent(CarSpawnEvent event) {
		// TODO: What exactly does this do
		// TODO: handle event
		// Create a Car
		// Create CarEnterEvent for it
		// Calculate the time of the next car spawn
		// Create next CarSpawnEvent
	}

	private CheckIntersectionEvent handleCarEnterEvent(CarEnterEvent event) {
		// TODO: handle event
	}

	private void handleCarExitEvent(CarExitEvent event) {
		// TODO: handle event
	}

	private CarEvent handleCheckIntersectionEvent(CheckIntersectionEvent event){
		// TODO: handle event
	}

}
