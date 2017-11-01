package cars;

import java.util.Random;

public class PathFactory {

	private int n;
	private int m;
	private int maxTurns;
	private Random random;

	public PathFactory(int n, int m, Random random) {
		this.n = n;
		this.m = m;
		this.random = random;
		
		maxTurns = 2;
		if (n == 2 || m == 2) {
			maxTurns = 1;
		}
	}

	// TODO: Simplify this mess!
	public Path newPath() throws Exception {
		// Randomly select a start point
		boolean startAvenue = random.nextBoolean();
		int startIndex;
		boolean endAvenue;
		int endIndex;
		if (startAvenue) {
			startIndex = random.nextInt(m);
		} else {
			startIndex = random.nextInt(n);
		}

		// Randomly select a number of turns:
		int numTurns = random.nextInt(maxTurns + 1);
		int[] turns = new int[numTurns];
		if (numTurns == 0) {
			endIndex = startIndex;
			endAvenue = startAvenue;
		} else if (numTurns == 1) {
			// Any index is a valid turn
			if (startAvenue) {
				// Turn onto any street
				turns[0] = random.nextInt(n);
				endAvenue = false;
			} else {
				// Turn onto any Avenue
				turns[0] = random.nextInt(m);
				endAvenue = true;
			}
			endIndex = turns[0];
		} else {  // numTurns == 2
			// Pick an endIndex,
			// The second turn has the index of that Road
			// The first turn can be any Even number or any Odd number, depending
			// on the start and end indices
			endAvenue = startAvenue;
			if (startAvenue) {
				// start and end on an Avenue (different ones)
				endIndex = (startIndex + random.nextInt(m-1) + 1) % m;
			} else {
				// start and end on a Street (different ones)
				endIndex = (startIndex + random.nextInt(n-1) + 1) % n;
			}
			if (startIndex == endIndex) {
				throw new Exception("Path starts and ends on same Road!");
			}
			turns[1] = endIndex;
			
			// Select location for first turn:
			// on EvenIndexedAvenue, go left for > endIndex
			//   go right for < endIndex
			// on OddIndexedAvenue, go right for > endIndex
			//   go left for < endIndex
			//  ^^ turn on odd Streets
			// on EvenIndexedStreet, go left for > endIndex
			//   go right for < endIndex
			//  ... turn on odd Avenues
			if (startIndex < endIndex) {
				// go left (turn on odd Streets / even Avenues)
				if (startAvenue) {
					// turn on odd Streets:
					turns[0] = 1 + 2 * random.nextInt(n / 2);
					if (turns[0] == n) {  // TODO: make sure this check not necessary
						turns[0] = 1;
					}
				} else {
					// turn on even Avenues
					turns[0] = 2 * random.nextInt(m / 2);
					if (turns[0] == n) {
						turns[0] = 1;
					}
				}
			} else {
				// go right (turn on even Streets / odd Avenues)
				if (!startAvenue) {
					// turn on odd Avenues:
					turns[0] = 1 + 2 * random.nextInt(n / 2);
					if (turns[0] == n) {  // TODO: make sure this check not necessary
						turns[0] = 1;
					}
				} else {
					// turn on even Streets
					turns[0] = 2 * random.nextInt(m / 2);
					if (turns[0] == n) {
						turns[0] = 1;
					}
				}
			}
		}
		return new Path(startAvenue, endAvenue, startIndex, endIndex, turns);
	}
}
