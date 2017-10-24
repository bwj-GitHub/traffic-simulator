package grid;

public class Road {
	int index;
	boolean isAvenue;
	RoadSegment[] roadSegments;
	int firstRoadSegmentIndex;

	public Road(int index, boolean isAvenue, int[] segmentLengths) {
		this.index = index;
		this.isAvenue = isAvenue;
		this.initRoadSegments(segmentLengths);

		// Calculate index of first RoadSegment
		if (index % 2 == 0) {
			// Even Roads: NS/EW?
			this.firstRoadSegmentIndex = 0;
		} else {
			// Odd Roads: SN/WE?
			this.firstRoadSegmentIndex = this.roadSegments.length - 1;
		}
	}

	private void initRoadSegments(int[] segmentLengths) {
		int numSegments = segmentLengths.length;
		roadSegments = new RoadSegment[numSegments];
		for (int i = 0; i < numSegments; i++) {
			roadSegments[i] = new RoadSegment(index, i, segmentLengths[i],
					isAvenue);
		}
	}
}
