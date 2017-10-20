package simulator;

import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Random;

import events.Event;
import events.carEvents.*;
import events.lightEvents.*;
import grid.TrafficGrid;
import lights.TrafficLightScheduler;
import simulator.Config;
import traffic.CarFactory;

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

	public TrafficSimulator(Config config){
		// TODO: Initialize TrafficGrid and TrafficLightScheduler
		this.config = config;
		this.random = new Random(config.randomSeed);
		this.tls = new TrafficLightScheduler(config, random);
		this.grid = new TrafficGrid(config, random);
		this.eventQueue = new EventQueue();
	}

	public void run(){
		eventQueue.add(new CarSpawnEvent(0.0f));

		float timelimit = config.timelimit;
		while (((Event) eventQueue.peek()).time() < timelimit) {
			Event nextEvent = (Event) eventQueue.poll();
			Event[] futureEvents;
			if (nextEvent instanceof CarEvent) {
				futureEvents = grid.handleEvent(nextEvent);
			} else if (nextEvent instanceof LightEvent) {
				futureEvents = tls.handleEvent(nextEvent);
			} else {
				throw new Exception();
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
