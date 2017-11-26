package events;

import grid.PathElement;
import grid.TrafficLight;

public class LightEvent extends Event {
	
	PathElement t;
	
	public LightEvent(PathElement light, int time)  
	{
		super(time);
		t=light;
	}
	
	
	/*
	@Override
	public int compareTo(Event e) {
		
			if(time==e.time)
				return 0;
			else if(time > e.time)
				return 1;
			else return -1;
		
	}
	*/
	
	
	public PathElement getLight()
	{
		return t;
	}

}
