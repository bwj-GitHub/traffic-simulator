package events.lightEvents;

import events.Event;
import grid.Intersection;

public class LightEvent extends Event {

	public Intersection intersection;

	public LightEvent(float time, Intersection intersection) {
		super(time);
		this.intersection = intersection;
	}

	public String toString() {
		return "LightEvent(" + time() + "s, " + intersection.toString() + ")";
	}


}
