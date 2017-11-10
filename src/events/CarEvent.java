package events;

/**
 * A generic Event for creating or updating Cars; Events for updating Cars should
 * extend this.
 * @author brandon
 *
 */
public class CarEvent extends Event{

	public CarEvent(eventtypeenum e, int t) {
		super(e, t);
		// TODO Auto-generated constructor stub
	}

}
