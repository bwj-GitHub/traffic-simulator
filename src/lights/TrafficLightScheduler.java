package lights;

import java.util.ArrayList;
import java.util.Random;

import events.Event;
import events.EventHandler;
import events.carEvents.CarEvent;
import events.lightEvents.LightEvent;
import grid.Intersection;
import simulator.Config;

public class TrafficLightScheduler implements EventHandler{
	Config config;
	Random random;
	Intersection[][] intersections;
	float greenTime;
	float yellowTime;

	public TrafficLightScheduler(Config config, Random random,
			Intersection[][] intersections) {
		this.config = config;
		this.random = random;
		this.intersections = intersections;
		this.greenTime = config.greenTime;
		this.yellowTime = config.yellowTime;
	}

	@Override
	public Event[] handleEvent(Event event) {
		LightEvent curEvent = (LightEvent) event;
		LightEvent nextEvent;
		Intersection intersection = curEvent.intersection;

		// Update Light colors (and TrafficQueue):
		CarEvent[] carEvents = intersection.handleLightEvent(curEvent);

		// Generate next LightEvent
		// NOTE: The lights will change state after either green or yellow Time
		if (intersection.hasYellowLight()) {
			nextEvent = new LightEvent(event.time() + yellowTime, intersection);
		} else {
			nextEvent = new LightEvent(event.time() + greenTime, intersection);
		}

		// Combine CarEvents with LightEvent:
		if (carEvents != null) {
			Event[] nextEvents = new Event[carEvents.length + 1];
			nextEvents[0] = nextEvent;
			for (int i = 0; i < carEvents.length; i++) {
				nextEvents[i+1] = carEvents[i];
			}
			return nextEvents;
		} else {
			return new Event[] {nextEvent};
		}
	}

	/**
	 * Randomly set initial light colors and return the list of initial LightEvents.
	 * @return
	 */
	public Event[] initLights() {
		int n = intersections.length;
		int m = intersections[0].length;
		boolean[][] initialLightStatus = new boolean[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				initialLightStatus[i][j] = random.nextBoolean();
			}
		}
		return initLights(initialLightStatus);
	}

	public Event[] initLights(boolean[][] initialLightStatus) {
		int n = intersections.length;
		int m = intersections[0].length;
		ArrayList<LightEvent> lightEvents = new ArrayList<LightEvent>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				// Set initial LightColors:
				TrafficLight avenueLight = intersections[i][j].getTrafficLight(true);
				TrafficLight streetLight = intersections[i][j].getTrafficLight(false);

				if (initialLightStatus[i][j] == true) {
					// Avenue is Green, Street RED
					avenueLight.color = LightColor.GREEN;
					streetLight.color = LightColor.RED;
				} else {
					// Avenue is Red, Street GREEN
					avenueLight.color = LightColor.RED;
					streetLight.color = LightColor.GREEN;
				}
				lightEvents.add(new LightEvent(greenTime, intersections[i][j]));
			}
		}
		return lightEvents.toArray(new LightEvent[lightEvents.size()]);
	}
}
