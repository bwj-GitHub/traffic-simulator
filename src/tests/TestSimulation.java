package tests;

import java.util.ArrayList;
import java.util.Random;

import cars.CarFactory;
import cars.Path;
import events.Event;
import events.carEvents.CarExitEvent;
import events.carEvents.CarSpawnEvent;
import events.carEvents.CarUpdateEvent;
import events.lightEvents.LightEvent;
import grid.TrafficGrid;
import lights.LightColor;
import lights.TrafficLight;
import lights.TrafficLightScheduler;
import simulator.Config;
import simulator.EventQueue;
import simulator.InterarrivalTimeGenerator;
import simulator.Statistics;

/**
 * Run several test simulations with controlled Car generation.
 * @author brandon
 *
 */
public class TestSimulation {
	Config config;
	Random random;
	TrafficGrid grid;
	CarFactory carFactory;
	InterarrivalTimeGenerator interArrival;
	TrafficLightScheduler tls;
	EventQueue eventQueue;
	Statistics statistics;
	int verbosity;

	public TestSimulation(){
	}

	private void initGrid(ArrayList<Path> paths) {
		// 3x3, d=50, v = 5
		this.config = new Config(3, 3, 100, 0, 200, 5.0f, 25.0f, 5.0f, 1, 5, 50);
		this.random = new Random();

		// Initialize TrafficGrid
		this.carFactory = new MockCarFactory(config.nRows, config.nCols, paths);
		this.interArrival = new InterarrivalTimeGenerator(config.lambda,
				random);
		this.grid = new TrafficGrid(config, carFactory, interArrival);

		// Initialize TrafficLightScheduler:
		this.tls = new TrafficLightScheduler(config, random, grid.intersections);
	}

	// TODO: Document how cars are expected to behave
	/*
	 * Insert a car, with a known path, and check that it follows the correct
	 * path and exits at the correct time.
	 */
	// NOTE: Assumes no acceleration
	public void testSingleCar() {
		// Create predetermined paths:
		ArrayList<Path> paths = new ArrayList<Path>();
		paths.add(new Path(true, true, 1, 1, new int[] {}));   // A

		initGrid(paths);
		System.out.println("Testing single car:");
		// Initialize lights
		boolean[][] initialLights = new boolean[][] {{false, true, false},
				{false, true, false}, {false, false, false}};
		tls.initLights(initialLights);

		// Insert a Car:
		CarSpawnEvent event = new CarSpawnEvent(0.0f);
		Event[] nextEvents = grid.handleEvent(event);

		// Check Events:
		//  The first event indicates when the car will arrive at its first
		//  intersection; the other is a CarSpawnEvent
		CarUpdateEvent updateEvent = checkUpdateEvent(nextEvents[0], 10.0f, 2, 1);

		// Process CarUpdateEvent:
		nextEvents = grid.handleEvent(updateEvent);
		assert nextEvents[0] == null;  // The light is Red

		// Cycle Light at intersection[2][1]:
		TrafficLight light = grid.intersections[2][1].getTrafficLight(true);
		nextEvents = tls.handleEvent(new LightEvent(30.0f, light, LightColor.GREEN));
		updateEvent = checkUpdateEvent(nextEvents[1], 30.0f, 2, 1);

		// Check event for next Intersection:
		nextEvents = grid.handleEvent(updateEvent);
		checkUpdateEvent(nextEvents[0], 40.6f, 1, 1);

		// Check event for final Intersection:
		nextEvents = grid.handleEvent(updateEvent);
		checkUpdateEvent(nextEvents[0], 51.2f, 0, 1);

		// Check for exit event:
		nextEvents = grid.handleEvent(updateEvent);
		assert nextEvents[0] instanceof CarExitEvent;
		CarExitEvent exitEvent = (CarExitEvent) nextEvents[0];
		System.out.println(exitEvent.toString());
		assert exitEvent.time() <= 61.81f && exitEvent.time() >= 61.79;
	}
	
	/* Insert several cars with the same path, check that they queued properly
	 * and stuff.
	 */
	public void testQueuedCars() {
		// Create predetermined paths:
		ArrayList<Path> paths = new ArrayList<Path>();
		paths.add(new Path(true, true, 1, 1, new int[] {}));   // A
		paths.add(new Path(true, true, 1, 1, new int[] {}));   // A
		initGrid(paths);
		System.out.println("\nTesting queued cars:");

		// Initialize lights
		boolean[][] initialLights = new boolean[][] {{false, true, false},
				{false, true, false}, {false, false, false}};
		tls.initLights(initialLights);

		// Insert 2 Cars with the same entry point:
		CarSpawnEvent[] spawnEvents = new CarSpawnEvent[] {new CarSpawnEvent(0.0f),
				new CarSpawnEvent(0.5f)};
		CarUpdateEvent[] updateEvents = processCarSpawnEvents(spawnEvents);

		// Check Events of first car:
		CarUpdateEvent updateEvent = checkUpdateEvent(updateEvents[0], 10.0f, 2, 1);

		// Check Events of second car:
		CarUpdateEvent updateEvent2 = checkUpdateEvent(updateEvents[1], 10.5f, 2, 1);

		// Process CarUpdateEvent for car1:
		Event[] car1Events = grid.handleEvent(updateEvent);
		assert car1Events[0] == null;  // The light is Red

		// Process CarUpdateEvent for car2:
		Event[] car2Events = grid.handleEvent(updateEvent2);
		assert car2Events[0] == null;  // The light is still Red

		// Cycle Light at intersection[2][1]:
		TrafficLight light = grid.intersections[2][1].getTrafficLight(true);
		Event[] nextEvents = tls.handleEvent(
				new LightEvent(30.0f, light, LightColor.GREEN));
		// Expecting 1 LightEvent and 2 new carUpdateEvents
		updateEvent = checkUpdateEvent(nextEvents[1], 30.0f, 2, 1);
		updateEvent2 = checkUpdateEvent(nextEvents[2], 30.2f, 2, 1);

		// Check events for next Intersection:
		nextEvents = grid.handleEvent(updateEvent);
		checkUpdateEvent(nextEvents[0], 40.6f, 1, 1);
		nextEvents = grid.handleEvent(updateEvent2);
		checkUpdateEvent(nextEvents[0], 40.8f, 1, 1);

		// Good enough!
	}
	
	/* Insert several cars at the same start point, but with paths that would
	 * place them in different lanes.
	 */
	public void testMultipleQueues() {
		ArrayList<Path> paths = new ArrayList<Path>();
		paths.add(new Path(true, true, 1, 1, new int[] {}));  // A
		paths.add(new Path(true, false, 1, 2, new int[] {2})); // B
		paths.add(new Path(true, false, 1, 2, new int[] {2}));  // B
		paths.add(new Path(true, true, 1, 1, new int[] {}));  // A
		paths.add(new Path(true, true, 1, 1, new int[] {}));  // A
		paths.add(new Path(true, true, 1, 1, new int[] {}));  // A
		initGrid(paths);
		System.out.println("\nTesting multiple queues:");

		// Initialize lights
		boolean[][] initialLights = new boolean[][] {{false, true, false},
				{false, true, false}, {false, false, false}};
		tls.initLights(initialLights);

		// Insert 6 Cars with the same entry point:
		CarSpawnEvent[] spawnEvents = new CarSpawnEvent[] {
				new CarSpawnEvent(0.0f), new CarSpawnEvent(0.5f),  // A, B
				new CarSpawnEvent(1.0f), new CarSpawnEvent(1.5f),  // B, A
				new CarSpawnEvent(2.0f), new CarSpawnEvent(2.5f)}; // A, A
		CarUpdateEvent[] updateEvents = processCarSpawnEvents(spawnEvents);

		// Check each car's first updateEvent
		CarUpdateEvent updateEvent;
		CarUpdateEvent[] nextEvents = new CarUpdateEvent[6];
		float firstTime = 10.0f;  // Time first car will reach the intersection:
		for (int i = 0; i < 6; i++) {
			updateEvent = checkUpdateEvent((Event) updateEvents[i],
					firstTime + (i * .5f), 2, 1);
			nextEvents[i] = (CarUpdateEvent) grid.handleEvent(updateEvent)[0];
			assert nextEvents[i] == null;  // stuck at light
		}

		// Cycle Light Green
		TrafficLight light = grid.intersections[2][1].getTrafficLight(true);
		Event[] lightUpdateEvents;
		lightUpdateEvents = tls.handleEvent(
				new LightEvent(30.0f, light, LightColor.GREEN));
		for (int i = 1; i < 7; i++) {  // Note: first event is next LightUpdateEvent
			nextEvents[i-1] = (CarUpdateEvent) lightUpdateEvents[i];
		}
		
		// Check each car's next updateEvent:
		// Note: Queues are checked in order: left, middle, right; since cars 1 and 2
		//  are turning left, their Events should be first in nextEvents
		System.out.println("Checking for correct queue times:");
		// Car 1 (B)
		nextEvents[0] = checkUpdateEvent((Event) nextEvents[0],
				30.0f, 2, 1);
		// Car 2 (B)
		nextEvents[1] = checkUpdateEvent((Event) nextEvents[1],
				30.2f, 2, 1);
		// Car 0 (A)
		nextEvents[2] = checkUpdateEvent((Event) nextEvents[2],
				30.0f, 2, 1);
		// Car 3 (A)
		nextEvents[3] = checkUpdateEvent((Event) nextEvents[3],
				30.2f, 2, 1);
		// Car 4 (A)
		nextEvents[4] = checkUpdateEvent((Event) nextEvents[4],
				30.4f, 2, 1);
		// Car 5 (A)
		nextEvents[5] = checkUpdateEvent((Event) nextEvents[5],
				30.6f, 2, 1);
		
		// Process nextEvents and verify that cars move to appropriate next intersection:
		for (int i = 0; i < 6; i++) {
			nextEvents[i] = (CarUpdateEvent) grid.handleEvent((Event) nextEvents[i])[0];
		}
		System.out.println("Checking for correct turns:");
		// Car 1 (B)
		nextEvents[0] = checkUpdateEvent((Event) nextEvents[0],
				40.6f, 2, 0);
		// Car 2 (B)
		nextEvents[1] = checkUpdateEvent((Event) nextEvents[1],
				40.8f, 2, 0);
		// Car 0 (A)
		nextEvents[2] = checkUpdateEvent((Event) nextEvents[2],
				40.6f, 1, 1);
		// Car 3 (A)
		nextEvents[3] = checkUpdateEvent((Event) nextEvents[3],
				40.8f, 1, 1);
		// Car 4 (A)
		nextEvents[4] = checkUpdateEvent((Event) nextEvents[4],
				41.0f, 1, 1);
		// Car 5 (A)
		nextEvents[5] = checkUpdateEvent((Event) nextEvents[5],
				41.2f, 1, 1);
	}
	
	/**
	 * Pass multiple CarSpawnEvents to trafficGrid and return the resulting
	 * CarUpdateEvents (future CarSpawnEvents are ignored).
	 * @param events
	 * @return the list of CarUpdateEvents corresponding to events.
	 */
	private CarUpdateEvent[] processCarSpawnEvents(CarSpawnEvent[] events) {
		int nCars = events.length;
		CarUpdateEvent[] updateEvents = new CarUpdateEvent[nCars];
		for (int i = 0; i < nCars; i++) {
			Event[] nextEvents = grid.handleEvent(events[i]);
			updateEvents[i] = (CarUpdateEvent) nextEvents[0];
		}
		return updateEvents;
	}

	private boolean almostEqual(float a, float b) {
		if (Math.abs(a - b) < .01) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Assert that the given event occurs at time in intersection[row][col] and is
	 * a CarUpdateEvent.
	 * 
	 * @param event: the event in question.
	 * @param time: the (approximate) time that the event should occur.
	 * @param row: the row of the cars current Intersection.
	 * @param col: the column of the car's current Intersection.
	 * @return event, cast as a CarUpdateEvent
	 */
	private CarUpdateEvent checkUpdateEvent(Event event, float time, int row, int col) {
		// Check event for final Intersection:
		assert event instanceof CarUpdateEvent;
		CarUpdateEvent updateEvent = (CarUpdateEvent) event;
		System.out.println(updateEvent.toString());
		assert almostEqual(updateEvent.time(), time);
		assert updateEvent.intersectionRowIndex == row;
		assert updateEvent.intersectionColIndex == col;
		return updateEvent;
	}

	public static void main(String[] args) {
		TestSimulation test = new TestSimulation();
		test.testSingleCar();
		test.testQueuedCars();
		test.testMultipleQueues();
	}
}

