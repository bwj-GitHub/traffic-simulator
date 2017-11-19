package events.distributed;

import events.Event;

public class ExternalEvent extends Event{
	int nodeId;
	Event event;

	public ExternalEvent(int nodeId, Event event) {
		super(event.getTime());
		this.nodeId = nodeId;
		this.event = event;
	}
	
	/**
	 * Return an int indicating the relative position of the destination node,
	 * relative to the source node.
	 * 
	 * @return 0 if North; 1 if East; 2 if South; and, 3 if West.
	 */
	public int getRelativeDestinationIndex() {
		// NOTE: nodeId might be changed to an absolute node ID.
		// TODO: is nodeId always the relative index?
		return nodeId;
	}
}
