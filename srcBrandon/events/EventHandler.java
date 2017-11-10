package events;

public interface EventHandler {

	public Event[] handleEvent(Event event) throws Exception;

}
