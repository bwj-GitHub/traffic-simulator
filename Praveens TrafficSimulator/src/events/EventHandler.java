package events;

import java.util.List;

public interface EventHandler {
	List<Event> handleEvent(Event e);
}
