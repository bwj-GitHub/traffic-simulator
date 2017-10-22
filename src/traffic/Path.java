package traffic;

public class Path {

	public boolean startAvenue;  // Path begins on an Avenue
	public boolean endAvenue;    // Path ends on an Avenue

	// Indices into Avenue or Street array:
	// NOTE: Avenues alternate between NS and SN (beginning with NS);
	//	Streets alternate between EW and WE.
	public int startIndex;
	public int endIndex;

	public int[] turns;  // Indices of Avenues/Streets to turn down

	public Path(boolean startAvenue, boolean endAvenue,
			int startIndex, int endIndex, int[] turns) {
		this.startAvenue = startAvenue;
		this.endAvenue = endAvenue;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.turns = turns;
	}

	/**
	 * Return the appropriate lane, given the progress through the Path.
	 * @param pathIndex
	 * @return the appropriate lane for the Car to be in during the  RoadSegment
	 * 	indicated by the pathIndex.
	 */
	public int getLaneIndex(int pathIndex) {
		if (pathIndex == this.turns.length) {
			return 0;  // No more turns, stay in middle lane
		} else {
			// Determine whether car is on an Avenue or Street
			boolean onAvenue = this.onAvenue(pathIndex);
			int currentIndex = this.currentIndex(pathIndex);

			// TODO: Test this!
			int[] turnIndices = null;
			if (currentIndex % 2 == 0) {
				turnIndices = new int[] {0, 2};
			} else {
				turnIndices = new int[] {2, 0};
			}
			if (onAvenue) {
				return turnIndices[(this.turns[pathIndex] + 1) % 2];
			} else {
				return turnIndices[this.turns[pathIndex] % 2];
			}
		}
	}
	
	/**
	 * Return true if the car should be on an Avenue given the pathIndex.
	 * @param pathIndex
	 * @return
	 */
	private boolean onAvenue(int pathIndex) {
		boolean[] onAvenue;
		if (startAvenue) {
			onAvenue = new boolean[] {true, false};
		} else {
			onAvenue = new boolean[] {false, true};
		}
		return onAvenue[pathIndex % 2];
	}

	/**
	 * Return the index of the Avenue or Street that the car should be on given
	 * the pathIndex.
	 * @param pathIndex
	 * @return
	 */
	private int currentIndex(int pathIndex) {
		if (pathIndex == 0) {
			return this.startIndex;
		} else {
			return this.turns[pathIndex - 1];
		}
	}

}
