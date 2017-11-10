package events;
import grid.Car;

public class CarUpdateEvent extends CarEvent {
	private Car car;
	
	public CarUpdateEvent(Car c,eventtypeenum e,int time)
	{
		super(e,time);
		car=c;
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
