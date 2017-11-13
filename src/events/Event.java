package events;
//import java.util.*;

  public abstract class Event implements Comparable<Event> {

	int time;
	
	public Event(int time)
	{
		this.time = time;
	}
	
	public int compareTo (Event e)
	{
		if (this.time == e.time)
			return 0;
		else if (this.time > e.time)
			return 1;
		else return -1;
	}

	public int getTime()
	{
		return time;
	}
}
