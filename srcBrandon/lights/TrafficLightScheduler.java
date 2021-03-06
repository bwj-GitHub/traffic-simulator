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
	boolean selfManagedLights;
	int queueThreshold;

	public TrafficLightScheduler(Config config, Random random,
			Intersection[][] intersections) {
		this.config = config;
		this.random = random;
		this.intersections = intersections;
		this.greenTime = config.greenTime;
		this.yellowTime = config.yellowTime;
		this.selfManagedLights = config.selfManagedLights;
		this.queueThreshold = config.queueThreshold;
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

	@Override
	public Event[] handleEvent(Event event) {
		if (this.selfManagedLights) {
			return handleSelfManagedLights((LightEvent) event);
		} else {
			// Dumb (Static) Light Scheduling
			return handleLightChange((LightEvent) event);
		}
	}

	/**
	 * Handles LightEvents when using self-managed lights.
	 * 
	 * Algorithm:
	 * 	When a LightEvent is received, the queue size for the light that is currently
	 *   RED will be checked, if it is above some threshold, the lights will proceed
	 *   to change; otherwise, the intersection will increment its `numGreenExtensions`,
	 *   if it is below 5, the light will remain unchanged, otherwise, the light will
	 *   change -- this is to prevent a small number of cars to get stuck at an
	 *   unchanging red light.
	 * 
	 * To make the TrafficLights more efficient, a RED light will not be turned GREEN
	 *  if it has 0 Cars waiting in its queue.
	 * @param event
	 * @return
	 */
	private Event[] handleSelfManagedLights(LightEvent event) {
		Intersection intersection = event.intersection;
		TrafficLight redLight = intersection.getRedLight();
		int redQueueSize = redLight.getTotalQueueSize();

		// If TrafficLight is yellow, update lights:
		if (intersection.hasYellowLight()) {
			return handleLightChange(event);
		}

		if (redQueueSize == 0) {
			// Keep lights as is:
			intersection.numGreenExtensions += 1;
			return handleSkipLightUpdate(event);
		}
		else if (intersection.numGreenExtensions > 3 || redQueueSize > queueThreshold) {
			// Lights must change if they have been extended more than 5 times
			//  or the RED light's queue size is greater than the threshold.
			intersection.numGreenExtensions = 0;
			return handleLightChange(event);
		} else {
			// Keep light the same:
			intersection.numGreenExtensions += 1;
			return handleSkipLightUpdate(event);
		}
	}
	
	private Event[] handleLightChange(LightEvent event) {
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
		return combineEvents(nextEvent, carEvents);
	}

	private Event[] handleSkipLightUpdate(LightEvent event) {
		// Next LightEvent occurs after greenTime
		float nextEventTime = event.time() + greenTime;
		LightEvent nextLightEvent = new LightEvent(nextEventTime, event.intersection);
		return combineEvents(nextLightEvent, null);
	}

	/**
	 * Create an Event[] using the next LightEvent and the list of CarEvents.
	 * @param nextLightEvent
	 * @param carEvents
	 * @return
	 */
	private Event[] combineEvents(LightEvent nextLightEvent, CarEvent[] carEvents) {
		// Combine CarEvents with LightEvent:
		if (carEvents != null) {
			Event[] nextEvents = new Event[carEvents.length + 1];
			nextEvents[0] = nextLightEvent;
			for (int i = 0; i < carEvents.length; i++) {
				nextEvents[i+1] = carEvents[i];
			}
			return nextEvents;
		} else {
			return new Event[] {nextLightEvent};
		}
	}

}
