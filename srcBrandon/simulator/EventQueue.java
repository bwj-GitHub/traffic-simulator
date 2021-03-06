package simulator;

import java.util.PriorityQueue;

import events.Event;


public class EventQueue {
	
	private PriorityQueue<Event> queue;
	int verbosity = 1;
	
	public EventQueue(int verbosity) {
		this.queue = new PriorityQueue<Event>();
		this.verbosity = verbosity;
	}
	
	public void add(Event e) {
		if (e == null) {
			return;
		}
		if (verbosity > 1) {
			System.out.println("+" + e.toString());
		}
		queue.add(e);
	}

	public void add(Event[] events){
		if (events == null) {
			return;
		}
		for (Event e: events) {
			this.add(e);
		}
	}
	
	public Event peek() {
		return queue.peek();
	}
	
	public Event poll() {
		return queue.poll();
	}

}
