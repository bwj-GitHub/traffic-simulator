package events;

import grid.TrafficLight;

public class LightEvent extends Event {
	
	TrafficLight t;
	
	public LightEvent(TrafficLight light, int time) 
	{
		super(time);
		t=light;
	}
	
	public TrafficLight getLight()
	{
		return t;
	}

}
