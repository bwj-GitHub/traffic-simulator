package events.distributed;

import events.Event;

/**
 * An Event indicating that no other Events will be produced with a timestamp
 * smaller than this.getTime().
 * 
 * @author brandon
 *
 */
public class NullEvent extends Event {

	public NullEvent(int time) {
		super(time);
	}

	public String getStringRepresentation() {
		return String.format("NullEvent\t%d", this.time);
	}

}
