package simulator;
import java.io.*;
import java.util.*;

import events.CarSpawnEvent;
import events.CarUpdateEvent;
import events.Event;
import grid.Car;
import grid.CarFactory;
import grid.LightColor;
import grid.TrafficGrid;
import grid.TrafficLightScheduler;
import events.EventQueue;
import events.LightEvent;


public class TrafficSimulator {
	private Config config;
	private EventQueue eventQueue;
	private TrafficGrid trafficGrid;
	private TrafficLightScheduler trafficLightScheduler;
	private int algorithm;  // BJ: Is this used anywhere?
	private ArrayList<Car> cars;
	private int numavenues;
	private int numstreets;
	private CarFactory carFactory;

	// BJ: Don't initialize when declaring attributes
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
		eventQueue = new EventQueue();
		trafficGrid = new TrafficGrid(config);
		trafficLightScheduler = new TrafficLightScheduler();
		carFactory = new CarFactory(config);
		cars = new ArrayList<Car>();

		// Generate all CarSpawnEvents
		CarSpawnEvent[] carSpawnEvents = carFactory.generateCarSpawnEvent(config);
		eventQueue.add(carSpawnEvents);
	}


	public void run()
	{
		int currenttime=1;  // BJ: Do we really need to explicitly count time?
		int ccheck=1;  // BJ: What is this?
		while (currenttime<=config.timelimit)
		{
			if(eventQueue.peek() != null && currenttime==eventQueue.peek().getTime())
			{
				Event currentEvent;
				currentEvent = eventQueue.poll();
				if(currentEvent instanceof CarSpawnEvent)
				{
					Event updateEvent = handleCarSpawnEvent(currentEvent);
					eventQueue.add(updateEvent);
				}
				else if(currentEvent instanceof CarUpdateEvent)
				{ // When we see car update event in queue.
					CarUpdateEvent update = (CarUpdateEvent) currentEvent;
					ArrayList<Event> list=trafficGrid.handleCarUpdateEvent(update,currenttime,eventQueue);
					if(!list.isEmpty())
					{
						for(Event ev:list)
						{
							eventQueue.add(ev);
						}
					}
				}
				else // trafficlight update event.
				{
					lighteventcounter++;
					// For other algorithms except dumb scheduling.
					// Code for self managed scheduling. change light to green , other light to red.
					//System.out.println("We are in traffic light update event");
					LightEvent le= (LightEvent) currentEvent;
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
			if ((eventQueue.peek()==null || eventQueue.peek().getTime()>currenttime))
			{
				currenttime++;
				for(Car car:cars)
				{
					if(car.getState()==0)
						total_wait_time++;
				}
				// We need to update the traffic lights states.
				trafficLightScheduler.UpdateTrafficLights(trafficGrid,numavenues,numstreets,carlength,carspacing,carspeed,currenttime,eventQueue,ccheck);
				ccheck++;
			}
		}
	}


	public Event handleCarSpawnEvent(Event carSpawnEvent) {
		int eventTime = carSpawnEvent.getTime();
		id++;
		cars.add(carFactory.newcar(id, eventTime, trafficGrid));
		//Adding car to first traffic light:
		cars.get(id-1).path.get(0).addcar(cars.get(id-1));

		// Generating the Car's next CarUpdateEvent:
		Event updateEvent = cars.get(id-1).generateCarUpdateEvent(eventTime);
		return updateEvent;
	}


	public void printStatistics() {
		//	System.out.println("Simulation complete with #cars:"+s.carslist.size());
		// Calculate the average time spent in grid for cars that exited:
		int count=0;
		int sum=0;
		float avg=0;
		for(Car c: cars)
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
		avg=(float) total_wait_time / cars.size();
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

		//System.out.println("The time of first event is:"+s.eventqueue.peek().getTime());
		// We have generated car creation events , Now we can start with the simulation.
		s.run();
		s.printStatistics();
	}
}
