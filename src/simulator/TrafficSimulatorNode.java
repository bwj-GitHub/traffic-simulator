package simulator;

import java.util.ArrayList;

import events.CarEvent;
import events.CarSpawnEvent;
import events.Event;
import events.EventQueue;
import events.LightEvent;
import events.distributed.DistributedEventQueue;
import events.distributed.ExternalEventQueue;
import grid.Car;
import grid.CarFactory;
import grid.TrafficGrid;
import grid.TrafficLightScheduler;

/**
 * Runs a portion of a traffic simulation.
 * @author brandon
 *
 */
public class TrafficSimulatorNode extends TrafficSimulator{

	public TrafficSimulatorNode(Config config, DistributedEventQueue distributedEventQueue)
	{
		super(config, distributedEventQueue);
	}

	public static TrafficSimulatorNode buildTrafficSimulatorNode(Config config) {
		// Initialize an ExternalEventQueue for each connected node:
		ExternalEventQueue[] externalEventQueues;
		int numNodes = config.numNodes;
		for (int i = 0; i < numNodes; i++) {
			externalEventQueues[i] = new ExternalEventQueue();
		}
		
		// Create and return a DistributedEventQueue:
		DistributedEventQueue distributedEventQueue = new DistributedEventQueue(
				externalEventQueues);
	}

	public static void main(String[] args) {
		Config config = null;
		if (args.length > 0) {
			config = Config(args);
		} else {
			// TODO: Create default Config
		}

		// TODO: Create DistributedEventQueue
		TrafficSimulatorNode sim = buildTrafficSimulatorNode(config);
		sim.run();
	}

}
