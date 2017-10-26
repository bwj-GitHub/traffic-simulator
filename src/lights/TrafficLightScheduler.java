package lights;

import java.util.ArrayList;
import java.util.Random;

import events.Event;
import events.EventHandler;
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
		// NOTE: Light's colors change independently of one another;
		//  however, they should still change colors at the same time.
		// TODO: This will not work with different types of scheduling
		LightEvent curEvent = (LightEvent) event;
		LightEvent nextEvent;
		TrafficLight light = curEvent.light;
		if (curEvent.color == LightColor.GREEN) {
			// Set light green:
			light.color = LightColor.GREEN;
			nextEvent = new LightEvent(event.time() + greenTime, light, LightColor.YELLOW);
		} else if (curEvent.color == LightColor.YELLOW) {
			// Set light yellow:
			light.color = LightColor.YELLOW;
			nextEvent = new LightEvent(event.time() + yellowTime, light, LightColor.RED);
		} else {
			// Set light red:
			light.color = LightColor.RED;
			nextEvent = new LightEvent(event.time() + greenTime + yellowTime,
					light, LightColor.GREEN);
		}
		return new Event[] {nextEvent};
	}
	
	/**
	 * Set initial light colors and return the list of initial LightEvents.
	 * @return
	 */
	public Event[] initLights() {
		int n = intersections.length;
		int m = intersections[0].length;
		ArrayList<LightEvent> lightEvents = new ArrayList<LightEvent>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				// Set initial LightColors:
				TrafficLight avenueLight = intersections[i][j].getTrafficLight(true);
				TrafficLight streetLight = intersections[i][j].getTrafficLight(false);

				double r = random.nextDouble();
				if (r < .5) {
					avenueLight.color = LightColor.GREEN;
					lightEvents.add(new LightEvent(greenTime, avenueLight,
							LightColor.YELLOW));
					
					streetLight.color = LightColor.RED;
					lightEvents.add(new LightEvent(greenTime + yellowTime, streetLight,
							LightColor.GREEN));
				}
			}
		}
		return (Event[]) lightEvents.toArray();
	}
}
