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

	public TrafficSimulator(Config config){
		this.config = config;
		this.random = new Random(config.randomSeed);
		this.interArrival = new InterarrivalTimeGenerator(config.lambda, random);
		this.tls = new TrafficLightScheduler(config, random);
		this.grid = new TrafficGrid(config, random, interArrival);
		this.eventQueue = new EventQueue();
	}

	public void run(){
		// Insert initial Events:
		Event[] initialLightEvents = tls.initLights();
		eventQueue.add(initialLightEvents);
		eventQueue.add(new CarSpawnEvent(0.0f));

		// Handle Events until time limit or no Events are remaining:
		while (((Event) eventQueue.peek()).time() < config.timeLimit) {
			Event nextEvent = (Event) eventQueue.poll();
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
	}

	public static void main(String[] args) {
		String configFileName = "./config";
		if (args.length == 1) {
			configFileName = args[0];
		}

		// Create Config:
		Config config = null;
		try {
			config = Config.readConfigFile(configFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Start Simulation:
		TrafficSimulator trafficSimulator = new TrafficSimulator(config);
		trafficSimulator.run();
	}
}
