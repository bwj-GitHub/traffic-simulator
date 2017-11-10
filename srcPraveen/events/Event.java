package events;
//import java.util.*;

  public abstract class Event implements Comparable<Event> {

	int time;
	eventtypeenum type;
	
	public Event(eventtypeenum e,int t)
	{
		type=e;
		time=t;
	}
	
	public int compareTo (Event e)
	{
		if(this.time==e.time)
			return 0;
		else if(this.time > e.time)
			return 1;
		else return -1;
		
	}
	
	
	public enum eventtypeenum {carspawn,carupdate,trafficlightupdate }
	
	public eventtypeenum getEventType()
	{
	
		return type;
		
	}
	
	public int getTime()
	{
		return time;
	}
}
