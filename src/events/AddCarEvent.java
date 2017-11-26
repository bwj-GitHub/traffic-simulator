package events;

import grid.Car;

public class AddCarEvent extends CarEvent {
	
private Car car;
	
	public AddCarEvent(Car c, int time)
	{
		super(time);
		car = c;
	}
	
	public Car getCar() 
	{
		return car;
	}

}
