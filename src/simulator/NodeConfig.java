package simulator;

public class NodeConfig extends Config {
	
	public int nodeRow;    // row of this node
	public int nodeCol;    // col of this node
	public String providerURL;
	public String connectionFactoryName;

	public NodeConfig(String[] args) {
		super(args);
		this.nodeRow = Integer.parseInt(args[8]);
		this.nodeCol = Integer.parseInt(args[9]);
		this.providerURL = args[10];
		this.connectionFactoryName = args[11];
	}

	public NodeConfig(String args) {
		this(args.split(" "));  // args: n, m, time, lambda, nCars, roadGapSize
	}

	public NodeConfig(int nNodeRows, int nNodeCols, int nodeRow, int nodeCol,
			String providerURL, String connectionFactoryName,
			// args for super class:
			int rowsPerNode, int columnsPerNode, int time, float lambda, int rowGap,
			int colGap, int speed, int acceleration, int size, int spacing, int numcars,
			int redTime, int greenTime, int yellowTime) {
		super(rowsPerNode, columnsPerNode, time, lambda, rowGap, colGap, speed,
				acceleration, size, spacing, numcars, redTime, greenTime, yellowTime);
		this.nNodeCols = nNodeRows;
		this.nNodeCols = nNodeCols;
		this.providerURL = providerURL;
	}
}
