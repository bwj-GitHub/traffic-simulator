package events;
import grid.Car;

public class CarUpdateEvent extends CarEvent {
	private Car car;

	public CarUpdateEvent(Car c, int time)
	{
		super(time);
		car = c;
	}

	/**
	 * Initialize a CarUpdateEvent from a String from which the time and the
	 * Car can be parsed.
	 * @param eventString: a String of the form: "CarUpdateEvent [time] [carString]".
	 */
	public CarUpdateEvent(String eventString) {
		super();
		String[] parts = eventString.split(" ", 3);
		this.time = Integer.parseInt(parts[1]);
		this.car = Car(parts[2]);
	}

	public Car getCar() 
	{
		return car;
	}

}
