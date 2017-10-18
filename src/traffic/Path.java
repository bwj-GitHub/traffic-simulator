package traffic;

public class Path {

	public boolean startAvenue;
	public boolean endAvenue;

	// Indices into Avenue or Street array:
	public int startIndex;
	public int endIndex;

	public int[] turns;  // Indices of turns

	public Path(boolean startAvenue, boolean endAvenue,
			int startIndex, int endIndex, int[] turns) {
		this.startAvenue = startAvenue;
		this.endAvenue = endAvenue;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.turns = turns;
	}

}
