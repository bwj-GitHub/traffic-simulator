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

	// TODO: Refactor for simultaneous light updates
	@Override
	public Event[] handleEvent(Event event) {
		// NOTE: Light's colors change independently of one another;
		//  however, they should still change colors at the same time.
		// TODO: This will not work with different types of scheduling
		LightEvent curEvent = (LightEvent) event;
		LightEvent nextEvent;
		TrafficLight light = curEvent.light;

		// Update Light color (and TrafficQueue):
		CarEvent[] carEvents = light.updateLight((LightEvent) event);

		// Generate next LightEvent
		if (curEvent.color == LightColor.GREEN) {
			// Green turns to Yellow next
			nextEvent = new LightEvent(event.time() + greenTime, light, LightColor.YELLOW);
		} else if (curEvent.color == LightColor.YELLOW) {
			// Yellow to Red
			nextEvent = new LightEvent(event.time() + yellowTime, light, LightColor.RED);
		} else {
			// Red to Green
			nextEvent = new LightEvent(event.time() + greenTime + yellowTime,
					light, LightColor.GREEN);
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

	// TODO: Refactor for simultaneous light updates
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
					lightEvents.add(new LightEvent(greenTime, avenueLight,
							LightColor.YELLOW));
					streetLight.color = LightColor.RED;
					lightEvents.add(new LightEvent(greenTime + yellowTime, streetLight,
							LightColor.GREEN));
				} else {
					// Avenue is Red, Street GREEN
					avenueLight.color = LightColor.RED;
					lightEvents.add(new LightEvent(greenTime + yellowTime, avenueLight,
							LightColor.GREEN));
					streetLight.color = LightColor.GREEN;
					lightEvents.add(new LightEvent(greenTime, streetLight,
							LightColor.YELLOW));
				}
			}
		}
		return lightEvents.toArray(new LightEvent[lightEvents.size()]);
	}
}
