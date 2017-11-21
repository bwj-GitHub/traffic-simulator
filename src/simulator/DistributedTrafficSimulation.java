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
	
	public DistributedTrafficSimulation() {
		
	}

	public static void main(String[] args) {
		// args: numNodeRows numNodeCols n m numCars lambda algorithm
		// Parse args:
		int numNodeRows = Integer.parseInt(args[0]);
		int numNodeCols = Integer.parseInt(args[1]);
		int nRowsPerNode = Integer.parseInt(args[2]);
		int nColsPerNode = Integer.parseInt(args[3]);
		int numCars = Integer.parseInt(args[4]);
		float lambda = Float.parseFloat(args[5]);
		int algorithm = Integer.parseInt(args[6]);

		// TODO: Connect to slave queues:

		// Send start/config. message to each node required for simulation:
		for (int i = 0; i < numNodeRows; i++) {
			for (int j = 0; j < numNodeCols; j++) {
				
			}
		}
		
		// Wait for message from each node with results:
		// NOTE: each node will respond with the number of cars that exited the
		//  (distributed) grid in its portion of the grid and the average time-in-grid
		//  of those cars.
		
		// Aggregate results:
		
		
		// Display results:
		
	}
}
