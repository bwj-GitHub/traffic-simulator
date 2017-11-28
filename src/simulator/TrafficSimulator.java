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
	public int numCarsExited;
	private Config config;
	private EventQueue eventQueue;
	private TrafficGrid trafficGrid;
	private TrafficLightScheduler trafficLightScheduler;
	private int algorithm;
	private ArrayList<Car> carslist;
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
		this(config, new EventQueue());
	}

	public TrafficSimulator(Config config, EventQueue eventQueue) {
		this.config=config;
		this.numavenues=config.numrows;
		this.numstreets=config.numcol;
		this.carspeed=config.carspeed;
		this.carlength=config.carsize;
		this.carspacing=config.carspacing;
		this.eventQueue = eventQueue;
		trafficGrid = new TrafficGrid(config);
		trafficLightScheduler = new TrafficLightScheduler();
		carFactory = new CarFactory(config);
		carslist = new ArrayList<Car>();
		numCarsExited = 0;

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
					CarUpdateEvent update= (CarUpdateEvent) currentEvent;
					ArrayList<Event> list=trafficGrid.handleCarUpdateEvent(update,currenttime,eventQueue,4);
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
					le.getLight().setlighttogreenevent(carspeed,carlength,carspacing,4);
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
				for(Car car:carslist)
				{
					if(car.getState()==0)
						total_wait_time++;
				}
				// We need to update the traffic lights states.
				trafficLightScheduler.UpdateTrafficLights(trafficGrid,numavenues,numstreets,carlength,carspacing,carspeed,currenttime,eventQueue,ccheck,4);
				ccheck++;
			}
		}
	}

	
	public int getNumExitedCars() {
		// TODO: set value for this variable
		return this.numCarsExited;
	}


	public float getAverageTimeInGrid() {
		// TODO: calculate this
		return 0.0f;
	}


	public Event handleCarSpawnEvent(Event carSpawnEvent) {
		int eventTime = carSpawnEvent.getTime();
		id++;
		carslist.add(carFactory.newcar(id, eventTime, trafficGrid));
		//Adding car to first traffic light:
		carslist.get(id-1).path.get(0).addcar(carslist.get(id-1));

		// Generating the Car's next CarUpdateEvent:
		Event updateEvent = carslist.get(id-1).generateCarUpdateEvent(eventTime);
		return updateEvent;
	}


	public void printStatistics() {
		float avg = getMeanTimeInGrid();
		float std = this.getStDevOfTimeInGrid(avg);
		int count = 0;
		for (Car c: carslist) {
			if (c.hasCarExited()) {
				count += 1;
			}
		}

		System.out.println(String.format("%d Cars exited the grid, with an average time in " +
				"grid of %f (stdev=%f).", count, avg, std));
		System.out.println("Number of traffic light update events are :" + lighteventcounter);
		avg=(float) total_wait_time / carslist.size();
		System.out.println("The avg time spent by cars at traffic light is:"+avg);
	}

	private float getMeanTimeInGrid() {
		float sum = 0;
		int count = 0;
		for (Car c: carslist) {
			if (c.hasCarExited()) {
				sum += c.getTimeInGrid();
				count += 1;
			}
		}
		return sum / count;
	}
	
	private float getStDevOfTimeInGrid(float meanTimeInGrid) {
        float squaredSum = 0.0f;
        int count = 0;
 
        for (Car c: carslist)
        	if (c.hasCarExited()) {
        		squaredSum += Math.pow((c.getTimeInGrid() - meanTimeInGrid), 2);
        		count += 1;
        	}
        return (float) Math.sqrt(squaredSum / (count));
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
