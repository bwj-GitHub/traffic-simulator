package events.distributed;

import events.Event;
import events.EventQueue;
import simulator.Config;


/**
 * The Event Queue for a single node in a distributed simulation.
 * 
 * In addition to storing local Events in a PriorityQueue, it stores incoming
 * external Events for each connected node and it routes outgoing external
 * Events to the appropriate node.
 * @author brandon
 *
 */
public class DistributedEventQueue extends EventQueue {

	private Config config;
	private ExternalEventQueue[] externalEventQueues;
	private Event lastPolledEvent;

	public DistributedEventQueue(Config config, ExternalEventQueue[] externalEventQueues) {
		super();
		this.config = config;
		this.externalEventQueues = externalEventQueues;
		this.lastPolledEvent = null;
		
		// Send first NullEvents to connected nodes:
		for (int i = 0; i < this.externalEventQueues.length; i++) {
			NullEvent firstNullEvent = createNextNullEvent(i, 0);
			this.externalEventQueues[i].sendEventMessage(firstNullEvent);
		}
	}

	public void add(Event e)
	{
		if (e instanceof ExternalEvent) {
			ExternalEvent externalEvent = (ExternalEvent) e;
			transferEvent(externalEvent);
		} else {
			// Add to local Event queue:
			this.queue.add(e);
		}
	}

	public void add(Event[] events) {
		for (Event e: events) {
			add(e);
		}
	}

	public void remove(Event e)
	{
		queue.remove(e);
	}

	public Event peek()
	{
		// TODO: does peek need to call `updateLastSentEvents`?
		int lowestTimestampedEQ = findLowestEventsQueue();
		Event nextEvent = externalEventQueues[lowestTimestampedEQ].peek();
		if (queue.peek().getTime() < nextEvent.getTime()) {
			nextEvent = queue.peek();
		}
		return nextEvent;
	}

	public Event poll()
	{
		updateLastSentEvents();
		int lowestTimestampedEQ = findLowestEventsQueue();
		Event nextEvent = externalEventQueues[lowestTimestampedEQ].poll();
		if (queue.peek().getTime() < nextEvent.getTime()) {
			nextEvent = queue.poll();
		}
		lastPolledEvent = nextEvent;
		return nextEvent;
	}

	public int getSize()
	{
		int totalSize = queue.size();
		for (ExternalEventQueue externalQueue: externalEventQueues) {
			totalSize += externalQueue.getSize();
		}
		return totalSize;
	}

	/**
	 * Return the index of the ExternalEventQueue with the Event with the lowest timestamp.
	 * @return an index into this.externalEventQueues.
	 */
	private int findLowestEventsQueue() {
		int lowestTimestamp = Integer.MAX_VALUE;
		int eqIndex = -1;
		for (int i = 0; i < externalEventQueues.length; i++) {
			Event event = externalEventQueues[i].peek();
			if (event.getTime() < lowestTimestamp) {
				lowestTimestamp = event.getTime();
				eqIndex = i;
			}
		}
		return eqIndex;
	}

	/**
	 * Send event to the appropriate node.
	 * @param destinationIndex: the index of ExternalEventQueue managing the destination node.
	 * @param event: the Event to be sent.
	 */
	private void transferEvent(int destinationIndex, Event event) {
		this.externalEventQueues[destinationIndex].sendEventMessage(event);
	}

	/**
	 * Send event to the appropriate node.
	 * @param event: an ExternalEvent indicating the destination of the
	 * wrapped Event.
	 */
	private void transferEvent(ExternalEvent event) {
		int destinationIndex = event.getRelativeDestinationIndex();
		transferEvent(destinationIndex, event.event);
	}

	/**
	 * Ensure that all ExternalEventQueues have been sent an Event with a timestamp
	 * greater than time of the current state (last processed Event's timestamp).
	 */
	private void updateLastSentEvents() {
		for (int i = 0; i < externalEventQueues.length; i++) {
			int lastTimestamp = externalEventQueues[i].lastSentEvent.getTime();
			if (lastTimestamp <= lastPolledEvent.getTime()) {
				// A NullEvent must be sent
				NullEvent nullEvent = createNextNullEvent(i, lastTimestamp);
				transferEvent(i, nullEvent);
			}
		}
	}

	private NullEvent createFirstNullEvent(int direction) {
		// TODO: Write me!
		return null;
	}

	private NullEvent createNextNullEvent(int direction, int lastEventTime) {
		int velocity = config.carspeed;
		int distance = 0;
		// TODO: Check that I am using the appropriate distances
		if (direction == 0 || direction == 2) {
			distance = config.distrows;
		} else {
			distance = config.distcols;
		}
		// TODO: how should (distance / velocity) be rounded?
		int eventTime = lastEventTime + (distance / velocity);
		NullEvent nullEvent = new NullEvent(eventTime);
		return nullEvent;
	}
}
