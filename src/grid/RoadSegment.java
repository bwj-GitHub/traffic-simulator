package grid;

public class RoadSegment {
	public int roadIndex;
	public int segmentIndex;
	public float length;
	public boolean isAvenue;
	public boolean isExit;
	private Intersection outIntersection;

	public RoadSegment(int roadIndex, int segmentIndex, float length,
			boolean isAvenue, boolean isExit) {
		this.roadIndex = roadIndex;
		this.segmentIndex = segmentIndex;
		this.length = length;
		this.isAvenue = isAvenue;
		this.isExit = isExit;
		this.outIntersection = null;  // will be set during initialization of
									  //  its outIntersection.
	}
	
	public Intersection getOutIntersection(Intersection intersection) {
		return this.outIntersection;
	}

	public void setIntersection(Intersection intersection) {
		this.outIntersection = intersection;
	}
}
