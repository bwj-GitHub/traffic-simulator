package events;
import grid.Car;

public class CarUpdateEvent extends CarEvent {
	private Car car;
	
	public CarUpdateEvent(Car c, int time)
	{
		super(time);
		car = c;
	}
	
	
	/*
	@Override
	public int compareTo(Event e) {
		
			if(time==e.time)
				return 0;
			else if(time > e.time)
				return 1;
			else return -1;
		
	}
	*/
	
	public Car getCar() 
	{
		return car;
	}

}
