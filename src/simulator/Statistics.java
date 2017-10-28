package simulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import events.Event;
import events.carEvents.CarExitEvent;
import events.carEvents.CarSpawnEvent;
import events.carEvents.CarUpdateEvent;
import traffic.Car;

public class Statistics {
	
	ArrayList<Float> timesInGrid;
	float sumOfTimes;
	int numOfExits;

	HashSet<Integer> carIds;
	ArrayList<Car> cars;

	String logFileName;
	ArrayList<Event> loggedEvents;
	boolean filterCarSpawn;
	
	public Statistics() {
		this(null);
	}
	
	public Statistics(String filename) {
		carIds = new HashSet<Integer>();
		cars = new ArrayList<Car>();

		timesInGrid = new ArrayList<Float>();
		this.logFileName = filename;
		if (filename != null) {
			this.loggedEvents = new ArrayList<Event>();
		}
		
		this.filterCarSpawn = true;  // TODO: Set with Constructor
	}
	
	public void log(Event event) {
		// Log Car in-grid times:
		if (event instanceof CarExitEvent) {
			CarExitEvent exitEvent = (CarExitEvent) event;
			float enterTime = exitEvent.car.enterTime;
			float exitTime = exitEvent.time();
			float timeInGrid = exitTime - enterTime;
			this.timesInGrid.add(timeInGrid);
			this.cars.add(exitEvent.car);
			sumOfTimes += timeInGrid;
			numOfExits += 1;
		}
		// TODO: Log events differently?
		if (loggedEvents != null) {
			if (loggedEvents.size() > 5000) {
				return;  // Logging too many Events will eat up Memory!
			}
			if (filterCarSpawn && event instanceof CarSpawnEvent) {
				return;
			}
			loggedEvents.add(event);
		}
	}
	
	public void log(Event[] events) {
		for (Event e: events) {
			log(e);
		}
	}
	
	public void printStatistics() {
		System.out.println(String.format("AvergeTimeInGrid=%f (for %d Cars)",
				sumOfTimes/numOfExits, numOfExits));
		
		// Write Event log to file (?):
		if (loggedEvents != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName));
				// Print Cars:
				writer.write("Cars (that exited):\n");
				for (Car car: this.cars) {
					writer.write("Car " + car.id + ", Path=" + car.getPath().toString() + "\n");
				}

				// Print Event Log:
				writer.write("\nEvent Log:\n");
				for (Event e: loggedEvents) {
					writer.write(e.toString() + "\n");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
