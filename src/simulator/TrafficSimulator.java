package simulator;
import java.io.*;
import java.util.*;

import events.CarSpawnEvent;
import events.CarUpdateEvent;
import events.Event;
import events.Event.eventtypeenum;
import grid.Car;
import grid.CarFactory;
import grid.TrafficGrid;
import grid.TrafficLightScheduler;
import events.EventQueue;
import events.LightEvent;


public class TrafficSimulator {
	
	private Config config;
	// We are assigning the simulation time as 500 time units.
	private int totalsimtime=5000;
	private TrafficGrid trafficgrid;
	private TrafficLightScheduler trafficlightscheduler=new TrafficLightScheduler();
	private int algorithm;
	private EventQueue eventqueue=new EventQueue();
	private ArrayList<Car> carslist = new ArrayList<Car>();
	//private ArrayList<Integer> exitedcarslist=new ArrayList<Integer>();
	private int numavenues;
	private int numstreets;
	private int distrows=100;
	private int distcolumns=100;
	private CarFactory factory=new CarFactory();
	private int id=0;
	private int carspeed=10;
	private int caracceleration=5;
	private int carlength=5;
	private int carspacing=1;
	private int lighteventcounter=0;
	
	
	
	public void run()
	{
		// We have initialized the grid and generated car spawn events, Now we will begin simulation.
		int currenttime=1;
	//	System.out.println("Before sim num of car events:"+eventqueue.getSize());
	//	eventqueue.print();
		while( currenttime<=totalsimtime)
		{
	//	System.out.println("in loop : "+currenttime);
			if(eventqueue.peek()!=null)
			{
			if(currenttime==eventqueue.peek().getTime())
			{
				Event e;
				e=eventqueue.poll();
				if(e.getEventType()==eventtypeenum.carspawn)
				{
					id++;
					carslist.add(factory.newcar(id,currenttime,numavenues,numstreets,trafficgrid,config));
					// After car spawn we need to create a carupdate event for spawned car.
					carslist.get(id-1).path.get(0).addcar(carslist.get(id-1));//Adding car to first traffic light.
					Event n=carslist.get(id-1).generateCarUpdateEvent(currenttime);
					eventqueue.add(n); // We added carupdate event into queue for that carspawn.
				}
				else if(e.getEventType()==eventtypeenum.carupdate)
				{ // When we see car update event in queue.
					CarUpdateEvent update= (CarUpdateEvent) e;
					ArrayList<Event> list=trafficgrid.handleCarUpdateEvent(update,currenttime,eventqueue);
					if(!list.isEmpty())
					{
						for(Event ev:list)
						{
							eventqueue.add(ev);
						}
					}
				}
				else // trafficlight update event.
				{
					
					lighteventcounter++;
					// For other algorithms except dumb scheduling.
					// Code for self managed scheduling. change light to green , other light to red.
					//System.out.println("We are in traffic light update event");
					LightEvent le= (LightEvent) e;
					//System.out.println("Setting light at the following position to green:");
					//le.getLight().printpos();
					//System.out.println(" ");
					le.getLight().setlighttogreen();
					//System.out.println("Setting other light at following position to red:");
					//le.getLight().printpos();
					//System.out.println(" ");
					le.getLight().getOtherLight().setlighttored();
					
				}
			}
			}
			//if((eventqueue.peek()!=null))	
			if( (eventqueue.peek()==null || eventqueue.peek().getTime()>currenttime))
			{
			currenttime++;
			// We need to update the traffic lights states.
			trafficlightscheduler.UpdateTrafficLights(trafficgrid,numavenues,numstreets,carlength,carspacing,carspeed,currenttime,eventqueue);
			}
		}
	
	}
	
	public TrafficSimulator(Config config)
	{
		this.config=config;
		this.totalsimtime=config.timelimit;
		this.numavenues=config.numrows;
		this.numstreets=config.numcol;
		this.distcolumns=config.distcols;
		this.distrows=config.distrows;
		this.carspeed=config.carspeed;
		this.caracceleration=config.caracceleration;
		this.carlength=config.carsize;
		this.carspacing=config.carspacing;
		
	}
	
	public static void main(String args[])
	{
		String configFileName = "src/config";
		if (args.length == 1) {
			configFileName = args[0];
		}
		Config configuration = null;
		try {
			configuration = Config.readConfigFile(configFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TrafficSimulator s=new TrafficSimulator(configuration);
		System.out.println("We are executing self managed scheduling");
		s.algorithm=1;
		// We will now initialize the grid
		s.trafficgrid=new TrafficGrid(s.numavenues,s.numstreets,s.config);
		// All the initialization is done. We will now create cars
		
		s.factory.generateCarSpawnEvent(s.eventqueue,s.config);
		//System.out.println("The time of first event is:"+s.eventqueue.peek().getTime());
		// We have generated car creation events , Now we can start with the simulation.
		s.run();
		
		System.out.println("Simulation complete with #cars:"+s.carslist.size());
		int count=0;
		int sum=0;
		float avg=0;
		for(Car c:s.carslist)
		{
			//count=0;
			//sum=0;
			if(c.hasCarExited())
			{
				count++;
				sum+=c.getTimeInGrid();
			}
			
		}
		if(count!=0)
		{
		avg=(float)sum/count;
		}
		
		System.out.println("The number of cars which exited grid are :"+count+", The avg time spent in grid is:"+avg);
		
		count=0;sum=0;avg=0;
		
		for(Car c:s.carslist)
		{
			//count=0;
			//sum=0;
			if(c.hasCarExited()&&c.getNumOfTurns()==0)
			{
				count++;
				sum+=c.getTimeInGrid();
			}
			
		}
		if(count!=0)
		{
		avg=(float)sum/count;
		}
		
	//	System.out.println("The average time spent by cars in the grid which took 0 turns is "+avg );
		
		count=0;sum=0;avg=0;
		
		for(Car c:s.carslist)
		{
			//count=0;
			//sum=0;
			if(c.hasCarExited())
			{
				count++;
				sum+=c.getTimeInGrid();
			}
			
		}
		if(count!=0)
		{
		avg=(float)sum/count;
		}
		System.out.println("Number of traffic light update events are :"+s.lighteventcounter);
	}


}
