package simulator;

import events.CarEvent;
import events.Event;
import events.EventQueue;
import events.ExternalEventQueue;
import events.LightEvent;
import grid.TrafficGrid;
import grid.TrafficLightScheduler;

public class TrafficSimulatorNode {
	
	public Config config;
	public TrafficGrid trafficGrid;
	public TrafficLightScheduler trafficLightScheduler;
	public EventQueue localEventQueue;
	public ExternalEventQueue[] externalEventQueues;
	
	public TrafficSimulatorNode(Config config) {
		
	}

	public void run() {
		Event nextEvent;
		Event[] futureEvents;
		do {
			nextEvent = getNextEvent();
			if (nextEvent instanceof CarEvent) {
				futureEvents = trafficGrid.handleEvent(nextEvent);
			} else if (nextEvent instanceof LightEvent) {
				futureEvents = trafficLightScheduler.handleEvent(nextEvent);
			} else {
				System.out.println("Unexpected event received!");
				break;
//				throw new Exception();
			}
			enqueueFutureEvents(futureEvents);
			
		} while (nextEvent.getTime() < config.timelimit);
		
	}

	/**
	 * Add local Events to the localEventQueue and send Events that should occur on
	 * a different TrafficSimulatorNode to the appropriate node.
	 * @param events: an Event[] containing the Events caused by the last processed
	 * Event.
	 */
	public void enqueueFutureEvents(Event[] events) {
		// TODO: Write me!
	}

	/**
	 * Return the Event from either the localEventQueue or any of the
	 * externalEventQueues with the lowest timestamp; this method will block until
	 * ALL externalEventQueues have at least one Event.
	 * @return the Event with the lowest timestamp.
	 */
	public Event getNextEvent() {
		// TODO: Write me!
	}
	
	public static void main(String[] args) {
		
	}

}
