package events;
import java.util.*;

public class EventQueue {

	private PriorityQueue<Event> queue;

	public EventQueue() {
		queue=new PriorityQueue<Event>();
	}

	public void add(Event e)
	{
		queue.add(e);
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
		return queue.peek();
	}

	public Event poll()
	{
		return queue.poll();
	}
	
	public int getSize()
	{
		return queue.size();
	}
	public void print()
	{
		System.out.println("Printing the car spawn events");
		System.out.println(queue);
	}
}
