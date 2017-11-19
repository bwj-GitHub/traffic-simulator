package simulator;

public class DistributedTrafficSimulation {

	public static void main(String[] args) {
		// args: numNodes n m numCars lambda algorithm
		// Create Config for distributed simulation:
		Config config = getConfig(args);

		// Create Config for each node:
		Config[] nodeConfig = new Config[config.numNodes];
		for (int i = 0; i < nodeConfig.length; i++) {
			nodeConfig = createNodeConfig(i, config);
		}

		// Start thread to launch each node:
		for (int i = 0; i < nodeConfig.length; i++) {
			// TODO: Start thread
		}

		// Accumulate statistics from each thread, once they finish:
		// TODO: Read results from each thread
		
		// Print results:
		// TODO: Print average-time-in-grid
	}

	private static Config getConfig(String[] args) {
		// TODO: Write me!
	}

	private static Config createNodeConfig(int index, Config config) {
		// TODO: Write me!
	}

	private static void startNode(Config nodeConfig) {
		// args: nodeId nodeN nodeM numCars lambda
		// TODO: Write me!
		// 
	}
}
