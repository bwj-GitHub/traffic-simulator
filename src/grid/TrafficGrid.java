package grid;

import java.util.*;

import events.CarSpawnEvent;
import events.CarUpdateEvent;
import events.Event;
import simulator.Config;
import events.EventQueue;
import events.LightEvent;


public class TrafficGrid  {
	private int numavenues;
	private int numstreets;
	Config config;
	CarFactory carfactory;
	Intersection[][] intersections;
	
	public TrafficGrid(Config config)
	{
		this.config=config;
		numavenues=config.numrows;
		numstreets=config.numcol;
		int i,j;
		intersections = new Intersection[numavenues][numstreets];
		for (i=0;i<numavenues;i++)
			for(j=0;j<numstreets;j++)
				intersections[i][j]=new Intersection(i,j,numavenues,numstreets,config);
	}

	
	public Intersection  getIntersection(int i,int j)
	{
		return intersections[i][j];
	}

	
	
	
	
	public ArrayList<Event> handleCarUpdateEvent(CarUpdateEvent event,int currenttime,EventQueue eq,int algorithm)
	{
	//	System.out.println("We are inside car update event with time:"+ currenttime);
		
		int n; //Number of cars ahead it at lane.
		ArrayList<Event> futureEvents=new ArrayList<Event>();
		Car c=event.getCar();
		if(config.debug)
		{
		System.out.println("Car with id:"+c.getId()+" has reached intersection and we are handling its car update event at time :"+ currenttime);
		System.out.println("Intersection is:");
		c.getCurrentLight().printpos();
		System.out.println("");
		System.out.println("The current light color is: "+c.getCurrentLight().getCurrentLightColor()+" Remaining light time is :"+c.getCurrentLight().getRemainingTime());
		}
		c.rest(); // Setting car to rest state.
		int n1=c.getCurrentLight().getcurrentposition(c);
		int distance;
		int time;
		TrafficLight currentlight=c.getCurrentLight();
		
		
		
		//For handling self managed scheduling we add the following code which increments the counter in current traffic light & checks if counter exceeds threshold.
		currentlight.incrementCounter();
		
		
		if(c.getCurrentLight().getCurrentLightColor()==LightColor.green && n1==1)
		{
			
			
			if(algorithm==4)
			{
			//We need to extend time if needed.
				ArrayList<Car> middlelane=c.getCurrentLight().getMiddleLane();
				int numcars=((c.getCurrentLight().getRemainingTime()*config.carspeed)+config.carspacing)/(config.carsize+config.carspacing);
				int num2=0;
				for(Car car:middlelane)
				{
					if(car.getState()==0)
						num2++;
				}
				if(num2>numcars)
				{
					int newtime;
					int x=((num2*(config.carsize+config.carspacing))-config.carspacing);
					if(x%config.carspeed==0)
						newtime=x/config.carspeed;
					else
						newtime=(x/config.carspeed)+1;
					//Extending current light time.
					c.getCurrentLight().setremaininglighttime(newtime);
					c.getCurrentLight().getOtherLight().setremaininglighttime(c.getCurrentLight().getOtherLight().getYellowTime()+newtime);
				}
				
				int k=0;
				int carstomove=(config.carspeed+config.carspacing)/(config.carsize+config.carspacing);
				for(int i=0;i<middlelane.size();i++)
				{
					Car car=currentlight.getMiddleLane().get(0);
					
					if(car.getState()==0)
					{
						
						if( car.getNextLightIndex()==car.path.size() ||(car.getCurrentLane()==Lane.middle && car.path.get(car.getNextLightIndex()).getMiddleLaneSize()<=car.getCurrentLight().getLaneLimit()) || ( car.getCurrentLane()!=Lane.middle &&(car.path.get(car.getNextLightIndex()).getNumberOfTurningCars()<=car.getCurrentLight().getLaneLimit())))
						{	 
						int check=car.getCurrentLight().removecar(car,currenttime+k*(1/carstomove)); // We removed car and added it to appropriate traffic light.
						// Now we need to create new carupdate event.
						car.moving(); // Car is in motion.
						if(check==1)
							futureEvents.add(car.generateCarUpdateEvent(currenttime+k*(1/carstomove)));
						}
					}
					k++;
				}
			//We need to loop through cars in middle lane and forward them.
			
			}
			
			else
			{
				if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
				{	 
				int check=c.getCurrentLight().removecar(c,currenttime); // We removed car and added it to appropriate traffic light.
				// Now we need to create new carupdate event.
				c.moving(); // Car is in motion.
				if(check==1)
					futureEvents.add(c.generateCarUpdateEvent(currenttime));
				}
			}
			//}
		}
		
		//Self-managed scheduling
		if((currentlight.getCounter()>currentlight.getThreshold()))
		{
			if(currentlight.getFlag()==false && currentlight.getCurrentLightColor()!=LightColor.green)
			{
			// Create traffic light update event with current time.
				if(config.debug)
				System.out.println("Creating traffic light update event and number of cars at that light are :"+currentlight.getCounter());
				Event lightevent=new LightEvent(currentlight, currenttime);
				futureEvents.add(lightevent);
				currentlight.getOtherLight().setFlag();
				currentlight.getOtherLight().setOtherLightEvent(lightevent);
			}
			else if((currentlight.getFlag()==true && (currentlight.getCounter()>currentlight.getOtherLight().getCounter())) && currentlight.getCurrentLightColor()!=LightColor.green)
			{
				if(config.debug)
				System.out.println("Creating traffic light update event and number of cars at that light are :"+currentlight.getCounter());
				futureEvents.add(new LightEvent(currentlight, currenttime));
			// By the current event creation if previous traffic light update event of other light is executed then we need to remove that event from eventqueue.
				eq.remove(currentlight.getOtherLightEvent()); //Remove event if it is present.
			}
			
			
		}
		//Handle carupdate
		
		return futureEvents;
	}
	
	
}
