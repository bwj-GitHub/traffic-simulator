package tests;

import java.util.ArrayList;

import cars.Car;
import cars.CarFactory;
import cars.Path;

public class MockCarFactory extends CarFactory {
	public ArrayList<Path> paths;

	public MockCarFactory(int n, int m, ArrayList<Path> paths) {
		super(n, m);
		this.paths = paths;
	}

	public Car newCar(float time) {
		int id = next_id;
		next_id++;
		return new Car(id, time, paths.remove(0));
	}

}
