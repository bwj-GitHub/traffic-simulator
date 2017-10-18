package simulator;

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

	public TrafficSimulator(Config config){
		// TODO: Initialize TrafficGrid and TrafficLightScheduler
	}

	public void run(){
		// TODO: Make CLI to get filename and run
		// TODO: Move this init stuff to the Constructor
		Config config = Config.readConfigFile(filename);
		Random random = new Random(config.randomSeed);

		TrafficLightScheduler tls = new TrafficLightScheduler(config, random);
		TrafficGrid grid = new TrafficGrid(config, random);

		EventQueue eventQueue = new EventQueue();
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

}
