package lights;

import java.util.Random;

import events.Event;
import events.EventHandler;
import grid.Intersection;
import simulator.Config;

public class TrafficLightScheduler implements EventHandler{
	Config config;
	Random random;
	Intersection[][] intersections;
	
	public TrafficLightScheduler(Config config, Random random,
			Intersection[][] intersections) {
		this.config = config;
		this.random = random;
		this.intersections = intersections;
	}

	@Override
	public Event[] handleEvent(Event event) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Set initial light colors and return the list of initial LightEvents.
	 * @return
	 */
	public Event[] initLights() {
		// TODO: Write me!
	}

}
