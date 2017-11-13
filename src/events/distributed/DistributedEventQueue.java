package events.distributed;

import java.util.PriorityQueue;

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

	private EventQueue localEventQueue;
	private ExternalEventQueue[] externalEventQueues;

	public DistributedEventQueue(ExternalEventQueue[] externalEventQueues) {
		localEventQueue = new EventQueue();
		this.externalEventQueues = externalEventQueues;
	}

	public void add(Event e)
	{
		// TODO: Events should have a flag for isLocal
		// TODO: Check destination of e
		// TODO: Write me!
	}

	public void add(Event[] events) {
		for (Event e: events) {
			add(e);
		}
	}

	public void remove(Event e)
	{
		localEventQueue.remove(e);
	}

	public Event peek()
	{
		// TODO: Check local and external queues
		return queue.peek();
	}

	public Event poll()
	{
		// TODO: Check local and external queues
		return queue.poll();
	}

	public int getSize()
	{
		int totalSize = localEventQueue.getSize();
		for (ExternalEventQueue externalQueue: externalEventQueues) {
			totalSize += externalQueue.getSize();
		}
		return totalSize;
	}

}
