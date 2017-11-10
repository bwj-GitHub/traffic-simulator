package simulator;
import java.io.*;
import java.util.*;

import events.CarSpawnEvent;
import events.CarUpdateEvent;
import events.Event;
import events.Event.eventtypeenum;
import grid.Car;
import grid.CarFactory;
import grid.LightColor;
import grid.TrafficGrid;
import grid.TrafficLightScheduler;
import events.EventQueue;
import events.LightEvent;


public class TrafficSimulator {
	private Config config;
	// We are assigning the simulation time as 500 time units.
	private TrafficGrid trafficGrid;
	private TrafficLightScheduler trafficLightScheduler;
	private int algorithm;
	private EventQueue eventqueue=new EventQueue();
	private ArrayList<Car> carslist = new ArrayList<Car>();
	//private ArrayList<Integer> exitedcarslist=new ArrayList<Integer>();
	private int numavenues;
	private int numstreets;
	private CarFactory factory=new CarFactory();
	private int id=0;
	private int carspeed=5;
	private int carlength=5;
	private int carspacing=1;
	private int lighteventcounter=0;
	private int total_wait_time=0;

	public TrafficSimulator(Config config)
	{
		this.config=config;
		this.numavenues=config.numrows;
		this.numstreets=config.numcol;
		this.carspeed=config.carspeed;
		this.carlength=config.carsize;
		this.carspacing=config.carspacing;
		trafficGrid = new TrafficGrid(config);
		trafficLightScheduler = new TrafficLightScheduler();
	}

	public void run()
	{
		// We have initialized the grid and generated car spawn events, Now we will begin simulation.
		int currenttime=1;
		int ccheck=1;
	//	System.out.println("Before sim num of car events:"+eventqueue.getSize());
	//	eventqueue.print();
		while( currenttime<=config.timelimit)
		{
			if(eventqueue.peek()!=null)
			{
			if(currenttime==eventqueue.peek().getTime())
			{
				Event e;
				e=eventqueue.poll();
				if(e.getEventType()==eventtypeenum.carspawn)
				{
					id++;
					carslist.add(factory.newcar(id,currenttime,numavenues,numstreets,trafficGrid,config));
					// After car spawn we need to create a carupdate event for spawned car.
					carslist.get(id-1).path.get(0).addcar(carslist.get(id-1));//Adding car to first traffic light.
					Event n=carslist.get(id-1).generateCarUpdateEvent(currenttime);
					eventqueue.add(n); // We added carupdate event into queue for that carspawn.
				}
				else if(e.getEventType()==eventtypeenum.carupdate)
				{ // When we see car update event in queue.
					CarUpdateEvent update= (CarUpdateEvent) e;
					ArrayList<Event> list=trafficGrid.handleCarUpdateEvent(update,currenttime,eventqueue);
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
					if(le.getLight().getCurrentLightColor()!=LightColor.green) // For coordinated scheduling.
					{
					le.getLight().setlighttogreenevent(carspeed,carlength,carspacing);
					//System.out.println("Setting other light at following position to red:");
					//le.getLight().printpos();
					//System.out.println(" ");
					le.getLight().getOtherLight().setlighttored();
					}
					
				}
			}
			}
			//if((eventqueue.peek()!=null))	
			if( (eventqueue.peek()==null || eventqueue.peek().getTime()>currenttime))
			{
				currenttime++;
				for(Car car:carslist)
				{
					if(car.getState()==0)
						total_wait_time++;
				}
				// We need to update the traffic lights states.
				trafficLightScheduler.UpdateTrafficLights(trafficGrid,numavenues,numstreets,carlength,carspacing,carspeed,currenttime,eventqueue,ccheck);
				ccheck++;
			}
		}
	}


	public void printStatistics() {
		//	System.out.println("Simulation complete with #cars:"+s.carslist.size());
		// Calculate the average time spent in grid for cars that exited:
		int count=0;
		int sum=0;
		float avg=0;
		for(Car c: carslist)
		{
			if (c.hasCarExited())
			{
				count++;
				sum += c.getTimeInGrid();
			}
		}
		if (count != 0)
		{
			avg=(float)sum/count;
		}
		System.out.println("The number of cars which exited grid are :"
				+ count + "; The avg time spent in grid is:" + avg);
		System.out.println("Number of traffic light update events are :" + lighteventcounter);
		avg=(float) total_wait_time / carslist.size();
		System.out.println("The avg time spent by cars at traffic light is:"+avg);
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

		TrafficSimulator s = new TrafficSimulator(configuration);
		//System.out.println("We are executing self managed scheduling");
		System.out.println("We are executing Coordinated scheduling");
		s.algorithm=1;

		// All the initialization is done. We will now create cars
		s.factory.generateCarSpawnEvent(s.eventqueue,s.config);
		//System.out.println("The time of first event is:"+s.eventqueue.peek().getTime());
		// We have generated car creation events , Now we can start with the simulation.
		s.run();
		s.printStatistics();
	}
}
