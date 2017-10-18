package simulator;

import java.util.PriorityQueue;

import events.Event;


public class EventQueue {
	
	private PriorityQueue<Event> queue;
	
	public EventQueue() {
		this.queue = new PriorityQueue<Event>();
	}
	
	public void add(Event e) {
		queue.add(e);
	}
	
	public void add(Event[] events){
		for (Event e: events) {
			queue.add(e);
		}
	}
	
	public Event peek() {
		return queue.peek();
	}
	
	public Event poll() {
		return queue.poll();
	}

}
