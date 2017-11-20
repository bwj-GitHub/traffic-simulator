package simulator;

import java.util.ArrayList;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

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

	// TODO: use NodeConfig instead of Config
	public static TrafficSimulatorNode buildTrafficSimulatorNode(Config config) {
		// Initialize an ExternalEventQueue for each connected node:
		int nNodeRows = config.nNodeRows;
		int nNodeCols = config.nNodeCols;
		int nodeRow = config.nodeRow;
		int nodeCol = config.nodeCol;

		ExternalEventQueue[] externalEventQueues = new ExternalEventQueue[4];
		// Build queue for North Neighbor:
		if (nodeRow > 0) {
			externalEventQueues[0] = buildExternalEventQueue(config,
					nodeRow, nodeCol, nodeRow + 1, nodeCol);
		}
		// Build queue for East Neighbor:
		if (nodeCol < nNodeCols - 1) {
			externalEventQueues[0] = buildExternalEventQueue(config,
					nodeRow, nodeCol, nodeRow, nodeCol + 1);
		}
		// Build queue for South Neighbor:
		if (nodeRow < nNodeRows - 1) {
			externalEventQueues[0] = buildExternalEventQueue(config,
					nodeRow, nodeCol, nodeRow - 1, nodeCol);
		}
		// Build queue for West Neighbor:
		if (nodeCol > 0) {
			externalEventQueues[0] = buildExternalEventQueue(config,
					nodeRow, nodeCol, nodeRow, nodeCol - 1);
		}

		// Create and return a DistributedEventQueue:
		DistributedEventQueue distributedEventQueue = new DistributedEventQueue(
				config, externalEventQueues);
	}

	public static ExternalEventQueue buildExternalEventQueue(Config config,
			int nodeI, int nodeJ, int connectedI, int connectedJ) {

		String factoryName = config.connectionFactoryName;
		// node[i][j] receives messages from  node[x][y] from: "queue-i.j-x.y"
		String inputQueueName = String.format("queue-%d.%d-%d.%d",
				nodeI, nodeJ, connectedI, connectedJ);
		String outputQueueName = String.format("queue-%d.%d-%d.%d",
				connectedI, connectedJ, nodeI, nodeJ);

		// Create Properties for connection:
		Properties connectionProps = new Properties();
		connectionProps.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.enterprise.naming.SerialInitContextFactory");
		connectionProps.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
		connectionProps.put(Context.PROVIDER_URL, config.providerURL);

		// Lookup ConnectionFactory by name and create and start a connection:
		InitialContext context = new InitialContext(connectionProps);
		QueueConnectionFactory connectionFactory =
				(QueueConnectionFactory) context.lookup(factoryName);
		QueueConnection connection = connectionFactory.createQueueConnection();
		connection.start();

		// Create a Queue session (shared by input and output queues):
		// TODO: Is it OK for the input and output queues to use the same session?
		QueueSession queueSession = connection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Create input and output Queue objects:
		Queue inputQueue = (Queue) context.lookup(inputQueueName);
		Queue outputQueue = (Queue) context.lookup(outputQueueName);

		// Create ExternalEventQueue:
		ExternalEventQueue eeq = new ExternalEventQueue(config, queueSession, inputQueue,
				outputQueue);
	}

	public static void main(String[] args) {
		Config config = null;
		if (args.length > 0) {
			// args: n, m, time, lambda, nCars, roadGapSize
			config = Config(args);
		} else {
			// TODO: Create default Config
		}

		TrafficSimulatorNode sim = buildTrafficSimulatorNode(config);
		sim.run();
	}

}
