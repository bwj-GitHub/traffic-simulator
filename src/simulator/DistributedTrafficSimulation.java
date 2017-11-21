package simulator;

public class DistributedTrafficSimulation {

	public static void main(String[] args) {
		// args: numNodes n m numCars lambda algorithm
		// Create Config for distributed simulation:
		Config config = new Config(args);

		// Create Config for each node:
		NodeConfig[][] nodeConfig = new NodeConfig[config.nNodeRows][config.nNodeCols];
		for (int i = 0; i < nodeConfig.length; i++) {
			for (int j = 0; j < nodeConfig[i].length; j++) {
				nodeConfig[i][j] = createNodeConfig(args, i, j);
			}
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

	private static NodeConfig createNodeConfig(String[] configArgs, int i, int j) {
		String[] nodeArgs = new String[configArgs.length + 4];
		int nArgs = configArgs.length;
		for (int k = 0; k < nArgs; k++) {
			nodeArgs[k] = configArgs[k];
		}
		nodeArgs[nArgs] = Integer.toString(i);
		nodeArgs[nArgs + 1] = Integer.toString(j);
		// TODO: make providerURL and factoryName variables?
		nodeArgs[nArgs + 2] = "http://localhost:4848/";
		nodeArgs[nArgs + 1] = "brandonsFactory";
		return new NodeConfig(nodeArgs);
	}

	private static void startNode(Config nodeConfig) {
		// args: nodeId nodeN nodeM numCars lambda
		// TODO: Write me!
		// 
	}
}
