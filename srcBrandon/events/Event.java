package events;

public class Event implements Comparable<Event> {

	private float time;

	public Event(float time) {
		this.time = time;
	}

	/* Return the time that the event should be executed. */
	public float time() {
		return this.time;
	}

	public int compareTo(Event other){
		return ((Float) this.time()).compareTo(other.time());
	}
	
	public String toString() {
		return "Event(" + time + ")";
	}
	
	
}
