package simulator;

import java.io.FileNotFoundException;
import java.util.Random;

import events.Event;
import events.carEvents.*;
import events.lightEvents.*;
import grid.TrafficGrid;
import lights.TrafficLightScheduler;
import simulator.Config;

/**
 * Initialize and run a traffic simulation.
 * 
 * @author brandon
 *
 */
public class TrafficSimulator {

	Config config;
	Random random;
	TrafficGrid grid;
	TrafficLightScheduler tls;
	EventQueue eventQueue;
	InterarrivalTimeGenerator interArrival;
	Statistics statistics;
	int verbosity;

	public TrafficSimulator(Config config){
		this.config = config;
		this.random = new Random(config.randomSeed);
		this.interArrival = new InterarrivalTimeGenerator(config.lambda, random);
		this.grid = new TrafficGrid(config, random, interArrival);
		this.tls = new TrafficLightScheduler(config, random, grid.intersections);
		// TODO: Set verbosity in config
		this.verbosity = 1;
		this.eventQueue = new EventQueue(verbosity);
		this.statistics = new Statistics();
	}

	public void run(){
		if (verbosity > 0) {
			System.out.println("Beginning simulation!");
		}

		// Insert initial Events:
		Event[] initialLightEvents = tls.initLights();
		CarSpawnEvent firstCarSpawn = new CarSpawnEvent(0.0f);
		statistics.log(initialLightEvents);
		statistics.log(firstCarSpawn);
		eventQueue.add(initialLightEvents);
		eventQueue.add(firstCarSpawn);

		// Handle Events until time limit or no Events are remaining:
		while (((Event) eventQueue.peek()).time() < config.timeLimit) {
			Event nextEvent = (Event) eventQueue.poll();
			statistics.log(nextEvent);
			if (verbosity > 0) {
				System.out.println("-" + nextEvent.toString());
			}

			Event[] futureEvents;
			if (nextEvent instanceof CarEvent) {
				futureEvents = grid.handleEvent(nextEvent);
			} else if (nextEvent instanceof LightEvent) {
				futureEvents = tls.handleEvent(nextEvent);
			} else {
				System.out.println("Unexpected event received!");
				break;
//				throw new Exception();
			}
			eventQueue.add(futureEvents);
		}
		
		System.out.println("\nFinished the Simulation!");
		statistics.printStatistics();
	}

	public static void main(String[] args) {
//		String configFileName = "./config";
		Config config = null;
		if (args.length == 1) {
			// Create Config from file:
			try {
				config = Config.readConfigFile(args[0]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
		} else {
			// Use simple config:
			// public Config(int n, int m, float timeLimit, long randomSeed, float lambda,
			// float acceleration, float maxVelocity, int d)
			config = new Config(2, 2, 150, 0, 2.0f, 1, 5, 50);
		}

		// Start Simulation:
		TrafficSimulator trafficSimulator = new TrafficSimulator(config);
		trafficSimulator.run();
	}
}
