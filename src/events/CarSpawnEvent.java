package events;


public class CarSpawnEvent extends Event {
	
	
	public CarSpawnEvent(eventtypeenum e, int t)
	{
		super(e, t);
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
	
	

}
