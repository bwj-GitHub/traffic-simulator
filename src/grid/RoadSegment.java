package grid;

public class RoadSegment {
	public int roadIndex;
	public int segmentIndex;
	public float length;
	public boolean isAvenue;
	public Intersection outIntersection;

	public RoadSegment(int roadIndex, int segmentIndex, float length,
			boolean isAvenue) {
		this.roadIndex = roadIndex;
		this.segmentIndex = segmentIndex;
		this.length = length;
		this.isAvenue = isAvenue;
		this.outIntersection = null;  // will be set during initialization of
									  //  its outIntersection.
	}

	public boolean isExit() {
		if (outIntersection == null) {
			return true;
		} else {
			return false;
		}
	}
}
