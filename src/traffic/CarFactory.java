package traffic;

import java.util.Random;

public class CarFactory {

	private PathFactory pathFactory;
	private int next_id;

	public CarFactory(int n, int m, Random random){
		this.pathFactory = new PathFactory(n, m, random);
		this.next_id = 0;
	}

	public CarFactory(int n, int m){
		this(n, m, new Random());
	}

	public Car newCar(float time) {
		// Randomly generate: a start point, an end point, and up to two turns.
		int id = next_id;
		next_id++;
		return new Car(id, time, pathFactory.newPath());
	}
}
