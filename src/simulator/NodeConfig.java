package simulator;

public class NodeConfig extends Config {
	
	public int nNodeRows;
	public int nNodeCols;
	public String providerURL;
	public String connectionFactoryName;
	
	public NodeConfig(String args) {
		super(args);  // args: n, m, time, lambda, nCars, roadGapSize

		String[] parts = args.split(" ");
		this.nNodeRows = Integer.parseInt(parts[6]);
		this.nNodeCols = Integer.parseInt(parts[7]);
		this.providerURL = parts[8];
		this.connectionFactoryName = parts[9];
		// TODO: Parse additional args
	}
	
	public NodeConfig(int nNodeRows, int nNodeCols, String providerURL,
			String connectionFactoryName, int rowsPerNode, int columnsPerNode,
			int time, float lambda, int rowGap,
			int colGap, int speed, int acceleration, int size, int spacing, int numcars,
			int redTime, int greenTime, int yellowTime) {
		super(rowsPerNode, columnsPerNode, time, lambda, rowGap, colGap, speed,
				acceleration, size, spacing, numcars, redTime, greenTime, yellowTime);
		this.nNodeCols = nNodeRows;
		this.nNodeCols = nNodeCols;
		this.providerURL = providerURL;
	}

}
