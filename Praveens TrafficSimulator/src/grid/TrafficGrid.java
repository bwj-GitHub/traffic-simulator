package grid;

import java.util.*;

import events.CarSpawnEvent;
import events.CarUpdateEvent;
import events.Event;
import events.Event.eventtypeenum;
import events.EventQueue;
import events.LightEvent;


public class TrafficGrid  {
	private int numavenues;
	private int numstreets;
	Config config;
	CarFactory carfactory;
	Intersection[][] intersections;
	
	public TrafficGrid(int a,int b,Config config)
	{
		this.config=config;
		numavenues=a;
		numstreets=b;
		int i,j;
		intersections=new Intersection[numavenues][numstreets];
		for (i=0;i<numavenues;i++)
			for(j=0;j<numstreets;j++)
				intersections[i][j]=new Intersection(i,j,numavenues,numstreets,config);
	}
	
	
	public Intersection  getIntersection(int i,int j)
	{
		return intersections[i][j];
	}
	
	ArrayList<Event> handleCarSpawnEvent(CarSpawnEvent event)
	{
		ArrayList<Event> l=new ArrayList<Event>();
		
		//Handle carspawn
		
		return l;
	}
	
	public ArrayList<Event> handleCarUpdateEvent(CarUpdateEvent event,int currenttime,EventQueue eq)
	{
	//	System.out.println("We are inside car update event with time:"+ currenttime);
		
		int n; //Number of cars ahead it at lane.
		ArrayList<Event> l=new ArrayList<Event>();
		Car c=event.getCar();
	//	System.out.println("Car with id:"+c.getId()+" has reached intersection and we are handling its car update event at time :"+ currenttime);
	//	System.out.println("Intersection is:");
	//	c.getCurrentLight().printpos();
	//	System.out.println("");
	//	System.out.println("The current light color is: "+c.getCurrentLight().getCurrentLightColor()+" Remaining light time is :"+c.getCurrentLight().getRemainingTime());
		c.rest(); // Setting car to rest state.
		int n1=c.getPosition();
		int distance;
		int time;
		TrafficLight currentlight=c.getCurrentLight();
		
		//For handling self managed scheduling we add the following code which increments the counter in current traffic light & checks if counter exceeds threshold.
		currentlight.incrementCounter();
		
		
		if(c.getCurrentLight().getCurrentLightColor()==lightcolor.green && c.getPosition()==1)
		{
			// Since trafficlight is green/yellow , We check if it can move to next traffic light and move it accordingly.
			/*switch(c.getCurrentLane()) // Get the current position of car in respective lane.
			{
			case left: n=c.getCurrentLight().getLeftLane().indexOf(c);
				break;
			case middle: n=c.getCurrentLight().getMiddleLane().indexOf(c);
				break;
			case right: n=c.getCurrentLight().getRightLane().indexOf(c);
				break;
			}*/
			// Get remaining light time and determine if within that time it can cross this intersection.
		//	n1--;
			//distance=n1*(c.getCarSize()+c.getCarSpacing());
			//time= distance/c.getCarSpeed();
			//if(time<=c.getCurrentLight().getRemainingTime())
			//{
				// We can proceed to move this car to next intersection.
				if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
				{	 
				int check=c.getCurrentLight().removecar(c,currenttime); // We removed car and added it to appropriate traffic light.
				// Now we need to create new carupdate event.
				c.moving(); // Car is in motion.
				if(check==1)
				l.add(c.generateCarUpdateEvent(currenttime));
				}
			//}
		}
		
		if((currentlight.getCounter()>currentlight.getThreshold()))
		{
			if(currentlight.getFlag()==false && currentlight.getCurrentLightColor()!=lightcolor.green)
			{
			// Create traffic light update event with current time.
			//	System.out.println("Creating traffic light update event and number of cars at that light are :"+currentlight.getCounter());
				Event lightevent=new LightEvent(currentlight,eventtypeenum.trafficlightupdate,currenttime);
				l.add(lightevent);
				currentlight.getOtherLight().setFlag();
				currentlight.getOtherLight().setOtherLightEvent(lightevent);
			}
			else if((currentlight.getFlag()==true && (currentlight.getCounter()>currentlight.getOtherLight().getCounter())) && currentlight.getCurrentLightColor()!=lightcolor.green)
			{
			//	System.out.println("Creating traffic light update event and number of cars at that light are :"+currentlight.getCounter());
				l.add(new LightEvent(currentlight,eventtypeenum.trafficlightupdate,currenttime));
			// By the current event creation if previous traffic light update event of other light is executed then we need to remove that event from eventqueue.
				eq.remove(currentlight.getOtherLightEvent()); //Remove event if it is present.
			}
			
			
		}
		//Handle carupdate
		
		return l;
	}
	
}
