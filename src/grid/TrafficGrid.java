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
		numavenues = config.numrows;
		numstreets = config.numcol;
		intersections = new Intersection[numavenues][numstreets];
		for (int i = 0; i < numavenues; i++)
		{
			for(int j = 0; j < numstreets; j++)
			{
				// BJ: why do I need numavenues and numstreets if I have config?
				intersections[i][j] = new Intersection(i, j, numavenues, numstreets, config);
			}
		}
	}

	
	public Intersection  getIntersection(int i, int j)
	{
		return intersections[i][j];
	}

	// BJ: Why is this being passed the EventQueue?
	public ArrayList<Event> handleCarUpdateEvent(CarUpdateEvent event, int currenttime, EventQueue eq)
	{
	//	System.out.println("We are inside car update event with time:"+ currenttime);

		int n; //Number of cars ahead it at lane.
		ArrayList<Event> futureEvents = new ArrayList<Event>();
		Car car = event.getCar();
	//	System.out.println("Car with id:"+c.getId()+" has reached intersection and we are handling its car update event at time :"+ currenttime);
	//	System.out.println("Intersection is:");
	//	c.getCurrentLight().printpos();
	//	System.out.println("");
	//	System.out.println("The current light color is: "+c.getCurrentLight().getCurrentLightColor()+" Remaining light time is :"+c.getCurrentLight().getRemainingTime());
		car.rest(); // Setting car to rest state.  // BJ: What is this?
		int n1=car.getPosition();
		int distance;
		int time;
		TrafficLight currentlight=car.getCurrentLight();

		//For handling self managed scheduling we add the following code which increments the counter in current traffic light & checks if counter exceeds threshold.
		currentlight.incrementCounter();

		if(car.getCurrentLight().getCurrentLightColor()==LightColor.green && car.getPosition()==1)
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
			// BJ: This if statement is too long
				if (car.getNextLightIndex() == car.path.size() ||
						(car.getCurrentLane() == Lane.middle &&
						car.path.get(car.getNextLightIndex()).getMiddleLaneSize() <= car.getCurrentLight().getLaneLimit()) ||
						(car.getCurrentLane()!=Lane.middle &&
						(car.path.get(car.getNextLightIndex()).getNumberOfTurningCars() <= car.getCurrentLight().getLaneLimit())))
				{	 
				int check = car.getCurrentLight().removecar(car,currenttime); // We removed car and added it to appropriate traffic light.
				// Now we need to create new carupdate event.
				car.moving(); // Car is in motion.
				if(check==1)
				futureEvents.add(car.generateCarUpdateEvent(currenttime));
				}
			//}
		}
		
		// BJ: What is this doing?
		// BJ: The TrafficLights should be controlled by the TrafficLightScheduler!
		if((currentlight.getCounter() > currentlight.getThreshold()))
		{
			if(currentlight.getFlag() == false &&  /* BJ: What is this flag? */
					currentlight.getCurrentLightColor() != LightColor.green)
			{
			// Create traffic light update event with current time.
			//	System.out.println("Creating traffic light update event and number of cars at that light are :"+currentlight.getCounter());
				Event lightevent=new LightEvent(currentlight, currenttime);
				futureEvents.add(lightevent);
				currentlight.getOtherLight().setFlag();
				currentlight.getOtherLight().setOtherLightEvent(lightevent);
			}
			else if((currentlight.getFlag()==true && (currentlight.getCounter()>currentlight.getOtherLight().getCounter())) && currentlight.getCurrentLightColor()!=LightColor.green)
			{
			//	System.out.println("Creating traffic light update event and number of cars at that light are :"+currentlight.getCounter());
				futureEvents.add(new LightEvent(currentlight, currenttime));
			// By the current event creation if previous traffic light update event of other light is executed then we need to remove that event from eventqueue.
				// BJ: NO! You should not be deciding that an Event won't be executed after it was placed in the EventQueue
				eq.remove(currentlight.getOtherLightEvent()); //Remove event if it is present.
			}
		}
		//Handle carupdate
		return futureEvents;
	}
	
}
