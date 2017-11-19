package events;
//import java.util.*;

  public abstract class Event implements Comparable<Event> {

	protected int time;
	
	/**
	 * Initialize an Event whose time will be set later.
	 */
	public Event() {
		time = -1;
	}

	public Event(int time)
	{
		this.time = time;
	}

	public Event(String eventString) {
		String[] parts = eventString.split("\t");
		this.time = Integer.parseInt(parts[1]);
	}

	public int compareTo (Event e)
	{
		if (this.time == e.time)
			return 0;
		else if (this.time > e.time)
			return 1;
		else return -1;
	}

	public String getStringRepresentation() {
		return String.format("Event\t%d", this.time);
	}

	public int getTime()
	{
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
}
