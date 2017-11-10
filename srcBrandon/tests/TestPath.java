package tests;

import cars.Path;

public class TestPath {
	
//	public Path(boolean startAvenue, boolean endAvenue,
//			int startIndex, int endIndex, int[] turns) {
	public static void testStraightPath2x2() {
		// Go straight down street 0
		Path path = null;
		try {
			path = new Path(false, false, 0, 0, new int[] {});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(path);
		assert path.getLaneIndex(0) == 1;
	}

	public static void testSingleLeftTurn3x3() {
		// Go straight down street 0 until intersection[0][0] and turn left down avenue
		Path path = null;
		try {
			path = new Path(false, true, 0, 0, new int[] {0});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(path);
		assert path.getLaneIndex(0) == 0;
		assert path.getLaneIndex(1) == 1;
	}

	public static void main(String[] args) {
		testStraightPath2x2();
		testSingleLeftTurn3x3();
	}

}
