package simulator;

/**
 * Starts a distributed traffic simulation.
 * 
 * This process is considered the 'master' process of a simulation; it will connect to the
 * messaging queues of the 'slave' nodes and send messages with config. information for
 * a simulation; 'slave' nodes will connect to the masters incoming message queue and
 * send messages with the results of the simulation. The master will aggregate the results.
 * 
 * @author brandon
 *
 */
public class DistributedTrafficSimulation {
	
	// TODO: How to store connections?
	public static final String[] defaultNodeNames = new String[] {
			"testNode1", "testNode2", "testNode3"};

	TwoWayMessenger[] messengers;

	public DistributedTrafficSimulation(String[] nodeNames) {
		// TODO: Allow for variable messenger names
		String factoryName = "brandonsFactory";
		String providerURL = "http://localhost:4848/";

		for (int i = 0; i < nodeNames.length; i++) {
	        String inputQueueName = String.format("queue-master-%s", nodeNames[i]);
	        String outputQueueName = String.format("queue-%s-master", nodeNames[i]);
			messengers[i] = new TwoWayMessenger(inputQueueName, outputQueueName,
					factoryName, providerURL);
		}
	}
	
	public void sendConfigurations(String[] args) {
		// TODO: Correct args:
		// args: numNodeRows numNodeCols n m time numCars lambda algorithm
		int numNodeRows = Integer.parseInt(args[0]);
		int numNodeCols = Integer.parseInt(args[1]);
		int nRowsPerNode = Integer.parseInt(args[2]);
		int nColsPerNode = Integer.parseInt(args[3]);
		int time = Integer.parseInt(args[4]);
		int numCars = Integer.parseInt(args[5]);
		float lambda = Float.parseFloat(args[6]);
		int algorithm = Integer.parseInt(args[7]);
		int roadGapSize = 100;  // TODO: Make arg

		int nextI = 0;  // next node to be assigned a portion of the TrafficGrid

		for (int i = 0; i < numNodeRows; i++) {
			for (int j = 0; j < numNodeCols; j++) {
				TwoWayMessenger nextMessenger = messengers[nextI];
				// Assign a node a portion of the TrafficGrid:
				// args: n, m, time, lambda, nCars, roadGapSize
				String configMessage = String.format("%d %d %d %f %d %d %d %d %s %s",
						nRowsPerNode, nColsPerNode, time, lambda, numCars, roadGapSize,
						i, j, nextMessenger.providerURL, nextMessenger.factoryName);
				nextMessenger.send(configMessage);
				nextI += 1;
			}
		}
	}

	public float getResults() {
		// Wait for message from each node with results:
		// NOTE: each node will respond with the number of cars that exited the
		//  (distributed) grid in its portion of the grid and the average time-in-grid
		//  of those cars.
		int nNodes = messengers.length;
		int numCarsExited = 0;
		float totalTimeInGrid = 0.0f;
		for (int i = 0; i < nNodes; i++) {
			try {
				String message = messengers[i].blockTillMessage(500);
				String[] mParts = message.split(" ");
				int nCars = Integer.parseInt(mParts[0]);
				float avgTime = Float.parseFloat(mParts[1]);
				numCarsExited += nCars;
				totalTimeInGrid += (nCars * avgTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		float avgTimeInGrid = totalTimeInGrid / numCarsExited;
		return avgTimeInGrid;
	}

	public static void main(String[] args) {
		// args: numNodeRows numNodeCols n m numCars lambda algorithm
		// TODO: Allow arg for roadGapSize
		// Parse (some) args:
		int numNodeRows = Integer.parseInt(args[0]);
		int numNodeCols = Integer.parseInt(args[1]);

		// Select nodes for simulation:
		// NOTE: It doesn't matter which node is used for what portion of the grid
		int numNodes = numNodeRows * numNodeCols;
		String[] nodeNames = new String[numNodes];
		for (int i = 0; i < numNodes; i++) {
			nodeNames[i] = defaultNodeNames[i];
		}

		// Create connections to slave queues:
		// NOTE: assumes two slave nodes with constant names
		DistributedTrafficSimulation dts = new DistributedTrafficSimulation(nodeNames);
		
		// Start simulation by sending configuration settings to each node:
		dts.sendConfigurations(args);
		
		// Aggregate result from response messages from each node:
		dts.getResults();
		
		// Display results:
		
	}
}
