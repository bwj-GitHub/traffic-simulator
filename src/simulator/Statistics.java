package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import events.Event;
import events.carEvents.CarExitEvent;
import traffic.Car;

public class Statistics {
	
	ArrayList<Float> timesInGrid;
	float sumOfTimes;
	int numOfExits;
	
	public Statistics() {
		timesInGrid = new ArrayList<Float>();
	}
	
	public void log(Event event) {
		// Log Car in-grid times:
		if (event instanceof CarExitEvent) {
			CarExitEvent exitEvent = (CarExitEvent) event;
			float enterTime = exitEvent.car.enterTime;
			float exitTime = exitEvent.time();
			float timeInGrid = exitTime - enterTime;
			this.timesInGrid.add(timeInGrid);
			sumOfTimes += timeInGrid;
			numOfExits += 1;
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
	}

}
