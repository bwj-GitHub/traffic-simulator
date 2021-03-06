package simulator;

import java.util.ArrayList;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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

	public TrafficSimulatorNode(NodeConfig config, DistributedEventQueue distributedEventQueue)
	{
		super(config, distributedEventQueue);
	}

	// TODO: use NodeConfig instead of Config
	public static TrafficSimulatorNode buildTrafficSimulatorNode(NodeConfig config) {
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

		// Create a DistributedEventQueue:
		DistributedEventQueue distributedEventQueue = new DistributedEventQueue(
				config, externalEventQueues);

		// Create and return a TrafficSimulatorNode:
		return new TrafficSimulatorNode(config, distributedEventQueue);
	}

	public static ExternalEventQueue buildExternalEventQueue(NodeConfig config,
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

		// Create Connections and Queues for an ExternalEventQueue:
		ExternalEventQueue eeq = null;
		try {
			// Lookup ConnectionFactory by name and create and start a connection:
			InitialContext context = new InitialContext(connectionProps);
			QueueConnectionFactory connectionFactory =
					(QueueConnectionFactory) context.lookup(factoryName);
			QueueConnection connection = connectionFactory.createQueueConnection();
			connection.start();  // TODO: When/how-to close this?
	
			// Create a Queue session (shared by input and output queues):
			// TODO: Is it OK for the input and output queues to use the same session?
			QueueSession queueSession = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);
	
			// Create input and output Queue objects:
			Queue inputQueue = (Queue) context.lookup(inputQueueName);
			Queue outputQueue = (Queue) context.lookup(outputQueueName);
	
			// Create ExternalEventQueue:
			eeq = new ExternalEventQueue(config, queueSession, inputQueue,
					outputQueue);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return eeq;
	}

	public static void startSimulation(TwoWayMessenger masterMessenger, String[] args) {
		System.out.println("Starting simulation...");
		System.out.println(String.format("args: %s", args.toString()));
		NodeConfig config = null;
		if (args.length > 0) {
			// args: n, m, time, lambda, nCars, roadGapSize
			config = new NodeConfig(args);
		} else {
			// TODO: Create default Config
			System.exit(-2);
		}

		TrafficSimulatorNode sim = buildTrafficSimulatorNode(config);
		sim.run();
		int numCarsExited = sim.getNumExitedCars();
		float averageTimeInGrid = sim.getAverageTimeInGrid();

		// Send results message:
		String resultsMessageText = String.format("%d %f",
				numCarsExited, averageTimeInGrid);
		masterMessenger.send(resultsMessageText);
	}

	public static void main(String[] args) {
		// Check that args are valid:
		// NOTE: these args only specify the node's name and the provider's URL,
		//  config. args for a simulation will be sent as a message.
		if (args.length < 2) {
			System.out.println("Missing required args: {nodeName, providerURL}");
			return;
		}
        String nodeName = args[0];
        String providerURL = args[1];
        String factoryName = "brandonsFactory";

        // Create two-way connection (using queues) between this node and the master:
        String inputQueueName = String.format("queue-%s-master", nodeName);
        String outputQueueName = String.format("queue-master-%s", nodeName);
        TwoWayMessenger masterMessenger = new TwoWayMessenger(inputQueueName,
        		outputQueueName, factoryName, providerURL);

		// Check for start/stop message:
		System.out.println("Waiting for Master...");
		int secondsIdle = 0;
		int maxIdle = 90;  // 1.5 minutes TODO: Make longer during demonstration
		boolean hasStopMessage = false;
		do {
			String message;
			// Check for next message:
			if (masterMessenger.getMessageBufferSize() == 0) {
				// No messages available, sleep:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				secondsIdle += 1;
				if (secondsIdle > 0 && secondsIdle % 30 == 0) {
					System.out.println("Time Idle: " + secondsIdle + "s");
				}
				continue;
			} else {
				// Parse message (stop message, or config (start) message):
				message = masterMessenger.getNextMessage();
				if (message.toLowerCase() == "stop") {
					hasStopMessage = true;
				} else {
					// message is config. args of new simulation:
					startSimulation(masterMessenger, message.split(" "));
				}
				secondsIdle = 0;
			}
		} while (secondsIdle < maxIdle && !hasStopMessage);

		// Simulation has ended:
		if (hasStopMessage) {
			System.out.println(String.format(
					"Simulation Node %s has received a stop message!", nodeName));
		} else {
			System.out.println(String.format(
					"Simulation Node %s has timed out!", nodeName));
		}
	}
}
