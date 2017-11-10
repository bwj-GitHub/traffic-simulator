package events;
import java.util.*;

public class EventQueue {
	
	private PriorityQueue<Event> queue=new PriorityQueue<Event>();
	
	public void add(Event e)
	{
		queue.add(e);
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
