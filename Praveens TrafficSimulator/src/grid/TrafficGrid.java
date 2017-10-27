package grid;

import java.util.*;

import events.CarSpawnEvent;
import events.CarUpdateEvent;
import events.Event;


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
	
	public ArrayList<Event> handleCarUpdateEvent(CarUpdateEvent event,int currenttime)
	{
	//	System.out.println("We are inside car update event with time:"+ currenttime);
		
		int n; //Number of cars ahead it at lane.
		ArrayList<Event> l=new ArrayList<Event>();
		Car c=event.getCar();
		System.out.println("Car with id:"+c.getId()+" has reached intersection and we are handling its car update event at time :"+ currenttime);
		System.out.println("Intersection is:");
		c.getCurrentLight().printpos();
		System.out.println("");
		System.out.println("The current light color is: "+c.getCurrentLight().getCurrentLightColor());
		c.rest(); // Setting car to rest state.
		int n1=c.getPosition();
		int distance;
		int time;
		if(c.getCurrentLight().getCurrentLightColor()==lightcolor.green)
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
			n1--;
			distance=n1*(c.getCarSize()+c.getCarSpacing());
			time= distance/c.getCarSpeed();
			if(time<=c.getCurrentLight().getRemainingTime())
			{
				// We can proceed to move this car to next intersection.
				int check=c.getCurrentLight().removecar(c,currenttime+time); // We removed car and added it to appropriate traffic light.
				// Now we need to create new carupdate event.
				c.moving(); // Car is in motion.
				if(check==1)
				l.add(c.generateCarUpdateEvent(currenttime+time));
			}
		}
		//Handle carupdate
		
		return l;
	}
	
}
