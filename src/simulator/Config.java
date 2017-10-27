package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Configuration options for traffic grid and scheduling.
 * @author brandon
 *
 */
public class Config {

	// NOTE: Avenues are NS and SN, streets are WE and EW
	public int nRows;  // n in project description
	public int nCols;  // m in project description
	float timeLimit;  // TODO: Read timelimit?
	long randomSeed; // TODO: Read this?

	float lambda;  // parameter for car arrival
	public float acceleration;
	float maxVelocity;

	// TODO: This should also include distance of edge Roads
	// NOTE: I updated in the constant distance config, but I have not changed
	//  documentation
	public int[] dRows;  // Distance between rows i and i+1 (in units c)
	public int[] dCols;  // Distance between cols i and i+1

	int[] nAvenueLanes;  // number of lanes in each avenue
	int[] nStreetLanes;  // number of lanes in each street
	
	public float greenTime;   // For dumb-scheduling
	public float yellowTime;  // "
	
	/* Create a Config with constant distance between roads and 3 lanes. */
	public Config(int n, int m, float timeLimit, long randomSeed, float lambda,
			float acceleration, float maxVelocity, int d){
		this.nRows = n;
		this.nCols = m;
		this.timeLimit = timeLimit;
		this.randomSeed = randomSeed;

		this.lambda = lambda;
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;

		this.dRows = new int[n+1];
		this.dCols = new int[m+1];
		this.nAvenueLanes = new int[n];
		this.nStreetLanes = new int[m];

		for (int i = 0; i < n+1; ++i){
			this.dRows[i] = d;
		}
		for (int i = 0; i < m+1; ++i){
			this.dCols[i] = d;
		}
		for (int i = 0; i < n; ++i){
			this.nStreetLanes[i] = 3;
		}
		for (int i = 0; i < n; ++i){
			this.nStreetLanes[i] = 3;
		}

		this.greenTime = 5.0f;
		this.yellowTime = 3.0f;
	}

	public Config(int n, int m, float timeLimit, long randomSeed, float lambda,
			float acceleration, float maxVelocity, int[] dRows, int[] dCols,
			int[] nAvenueLanes, int[] nStreetLanes){
		this.nRows = n;
		this.nCols = m;
		this.timeLimit = timeLimit;
		this.randomSeed = randomSeed;

		this.lambda = lambda;
		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;

		this.dRows = dRows;
		this.dCols = dCols;
		this.nAvenueLanes = nAvenueLanes;
		this.nStreetLanes = nStreetLanes;

		this.greenTime = 5.0f;
		this.yellowTime = 3.0f;
	}

	public static Config readConfigFile(String filename) throws FileNotFoundException{
		Scanner sc;
		sc = new Scanner(new File(filename));

		int n = sc.nextInt();
		int m = sc.nextInt();
		float timeLimit = sc.nextFloat();
		long randomSeed = sc.nextLong();
		float lambda = sc.nextFloat();
		float acceleration = sc.nextFloat();
		float velocity = sc.nextFloat();

		int[] dRows = new int[n-1];
		int[] dCols = new int[m-1];
		int[] nStreetLanes = new int[n];
		int[] nAvenueLanes = new int[m];

		// Read distances between streets:
		for (int i = 0; i < n-1; ++i) {
			dRows[i] = sc.nextInt();
		}
		// Read distances between avenues:
		for (int i = 0; i < m-1; ++i) {
			dCols[i] = sc.nextInt();
		}
		// Read number of Lanes in each street:
		for (int i = 0; i < n; ++i) {
			nStreetLanes[i] = sc.nextInt();
		}
		// Read number of Lanes in each avenue:
		for (int i = 0; i < n; ++i) {
			nAvenueLanes[i] = sc.nextInt();
		}

		sc.close();
		return new Config(n, m, timeLimit, randomSeed, lambda, acceleration,
				velocity, dRows, dCols, nStreetLanes, nAvenueLanes);
	}
}
