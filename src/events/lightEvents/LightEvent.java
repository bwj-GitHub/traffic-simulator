package events.lightEvents;

import events.Event;
import lights.LightColor;
import lights.TrafficLight;

public class LightEvent extends Event {
	
	public TrafficLight light;
	public LightColor color;

	public LightEvent(float time, TrafficLight light, LightColor color) {
		super(time);
		this.light = light;
		this.color = color;
	}

	public String toString() {
		return "LightEvent(" + time() + ", " + light.toString() + ", " + color +")";
	}


}
