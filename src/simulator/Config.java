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
	public float timeLimit;
	public long randomSeed;

	public int carSpawnLimit;
	public float lambda;      // average number of cars per second
	public float acceleration;
	public float maxVelocity;

	// TODO: This should also include distance of edge Roads
	// NOTE: I updated in the constant distance config, but I have not changed
	//  documentation
	public int[] dRows;  // Distance between rows i and i+1 (in units c)
	public int[] dCols;  // Distance between cols i and i+1

	public float greenTime;   // For dumb-scheduling
	public float yellowTime;  // "

	// TODO: Add to Constructor:
	public boolean selfManagedLights = true;
	public int queueThreshold = 50;  // For self-managed lights
	
	/* Create a Config with constant distance between roads and 3 lanes. */
	public Config(int n, int m, float timeLimit, long randomSeed,
			int carSpawnLimit, float lambda, float greenTime, float yellowTime,
			float acceleration, float maxVelocity, int d){
		// Create config without dRows or dCols:
		this(n, m, timeLimit, randomSeed, carSpawnLimit, lambda, greenTime, yellowTime,
				acceleration, maxVelocity, null, null);

		// Specify a constant distance, d, between all roads and avenues:
		this.dRows = new int[n+1];
		this.dCols = new int[m+1];
		for (int i = 0; i < n+1; ++i){
			this.dRows[i] = d;
		}
		for (int i = 0; i < m+1; ++i){
			this.dCols[i] = d;
		}
	}

	/* Full constructor. */
	public Config(int n, int m, float timeLimit, long randomSeed,
			int carSpawnLimit, float lambda, float greenTime, float yellowTime,
			float acceleration, float maxVelocity, int[] dRows, int[] dCols){
		this.nRows = n;
		this.nCols = m;
		this.timeLimit = timeLimit;
		this.randomSeed = randomSeed;

		this.carSpawnLimit = carSpawnLimit;
		this.lambda = lambda;

		this.greenTime = greenTime;
		this.yellowTime = yellowTime;

		this.acceleration = acceleration;
		this.maxVelocity = maxVelocity;

		this.dRows = dRows;
		this.dCols = dCols;
	}

	public static Config readConfigFile(String filename) throws FileNotFoundException{
		Scanner sc;
		sc = new Scanner(new File(filename));

		int n = sc.nextInt();
		int m = sc.nextInt();
		float timeLimit = sc.nextFloat();
		long randomSeed = sc.nextLong();

		int carSpawnLimit = sc.nextInt();
		float lambda = sc.nextFloat();

		float greenTime = sc.nextFloat();
		float yellowTime = sc.nextFloat();

		float acceleration = sc.nextFloat();
		float velocity = sc.nextFloat();

		int[] dRows = new int[n+1];
		int[] dCols = new int[m+1];

		// Read distances between streets:
		for (int i = 0; i < n+1; ++i) {
			dRows[i] = sc.nextInt();
		}
		// Read distances between avenues:
		for (int i = 0; i < m+1; ++i) {
			dCols[i] = sc.nextInt();
		}

		sc.close();
		return new Config(n, m, timeLimit, randomSeed, carSpawnLimit, lambda,
				greenTime, yellowTime, acceleration, velocity, dRows, dCols);
	}
}
