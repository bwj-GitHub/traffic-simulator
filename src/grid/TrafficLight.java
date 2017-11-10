package grid;

import java.util.ArrayList;

import events.Event;
import simulator.Config;

public class TrafficLight {
	
	private Config config;
	private int redtime=5;
	private int greentime=5;
	private int yellowtime=3;
	private int threshold=6;//Number of cars which turn light to green for traffic light.
	private int counter=0; // Number of cars at intersection at that current traffic light. 
	private int lanelimit=10;
	private int xpos;
	private int ypos;
	private int llcarstomove=0;
	private int mlcarstomove=0;
	private int rlcarstomove=0;
	private int otherlightcounter=0;
	private boolean flag=false;
	private LightColor currentlight;
	private int remainingtime;
	private TrafficDirection direction;
	private TrafficLight otherlight;
	private Event otherlightevent;
	private ArrayList<Car> list1=new ArrayList<Car>();
	private ArrayList<Car> list2=new ArrayList<Car>();
	private ArrayList<Car> list3=new ArrayList<Car>();
	
	//methods.
	public int getYellowTime()
	{
		return yellowtime;
	}
	
	public int getLaneLimit()
	{
		return lanelimit;
	}
	
	public void setLLCarsToMove(int n)
	{
		llcarstomove=n;
	}
	
	public int getLLCarsToMove()
	{
		return llcarstomove;
	}
	
	public void setOtherLightCounter(int n)
	{
		otherlightcounter=n;
	}
	
	public void setFlag()
	{
		flag=true;
	}
	
	public void unsetFlag()
	{
		flag=false;
	}
	
	public void setOtherLightEvent(Event e)
	{
		otherlightevent=e;
	}
	
	public Event getOtherLightEvent()
	{
		return otherlightevent;
	}
	
	public int getOtherLightCounter()
	{
		return otherlightcounter;
	}
	
	public boolean getFlag()
	{
		return flag;
	}
	
	public void decLLCarsToMove()
	{
		llcarstomove--;
	}
	
	public void setMLCarsToMove(int n)
	{
		mlcarstomove=n;
	}
	
	public int getMLCarsToMove()
	{
		return mlcarstomove;
	}
	
	public void decMLCarsToMove()
	{
		mlcarstomove--;
	}
	
	public void setRLCarsToMove(int n)
	{
		rlcarstomove=n;
	}
	
	public int getRLCarsToMove()
	{
		return rlcarstomove;
	}
	
	public void decRLCarsToMove()
	{
		rlcarstomove--;
	}
	
	public TrafficLight(Config c)
	{
		this.config=c;
		this.redtime=c.redtime;
		this.greentime=c.greentime;
		this.yellowtime=c.yellowtime;
	}
	
	//initialize  light to green
	public void setlighttogreen()
	{
		currentlight=LightColor.green;
		remainingtime=greentime;
	}
	
	public void setpos(int x,int y)
	{
		xpos=x;
		ypos=y;
	}
	
	public void printpos()
	{
		System.out.print(" ["+xpos+"]"+"["+ypos+"].");
	}
	
	//initialize light to red
	
	public int getThreshold()
	{
		return threshold;
	}
	
	public void incrementCounter()
	{
		counter++;
	}
	
	public void decrementCounter()
	{
		counter--;
	}
	
	public int getCounter()
	{
		return counter;
	}
	
	public void setlighttored()
	{
		currentlight=LightColor.red;
		remainingtime=redtime;
	}
	
	public void setlighttoyellow()
	{
		currentlight=LightColor.yellow;
		remainingtime=yellowtime;
	}
	
	public void setdirection(TrafficDirection d)
	{
		direction=d;
	}
	
	public void setOtherLight(TrafficLight light)
	{
		otherlight=light;
	}
	
	public TrafficLight getOtherLight()
	{
		return otherlight;
	}
	
	public TrafficDirection getTrafficDirection()
	{
		return direction;
	}
	
	public void addcar(Car c)  // Adds car to respective lane for that traffic light.
	{
		if (c.getNumOfTurns()==0)
		{
			list2.add(c);
			c.setposition(list2.size());
			c.setCurrentLane(Lane.middle);
		}	
		else if(c.getNumOfTurns()==1)
		{
			if(c.getLane()==Lane.left)
			{ 
				list1.add(c);
				c.setposition(list1.size());
				c.setCurrentLane(Lane.left);
			}
			else 
			{	
				list3.add(c);
				c.setposition(list3.size());
				c.setCurrentLane(Lane.right);
			}
				c.decTurnCount1();
		}
		else
		{
			if(c.getLane()==Lane.left)
			{ 
				list1.add(c);
				c.setposition(list1.size());
				c.setCurrentLane(Lane.left);
			}
			else 
			{
				list3.add(c);
				c.setposition(list3.size());
				c.setCurrentLane(Lane.right);
			}
			c.decTurnCount1();
			c.decTurnCount2();
		}
		c.setCurrentLight(this);
		c.incrementNextLightIndex();
	}
	
	public int removecar(Car c,int time)
	{
		c.getCurrentLight().decrementCounter();
		switch(c.getCurrentLane()) //We first remove car from current traffic light's lane.
		{	
		case left: list1.remove(c);
			break;
		case middle: list2.remove(c);
			break;
		case right:	list3.remove(c);
			break;
		}
		
		if(c.getNextLightIndex()==c.path.size()) // We have reached the end of the path.
		{
			// Exit grid event.
			//time+10 because it needs to travel 100 units distance after crossing last intersection before leaving grid.
			//c.getCurrentLight().decrementCounter();
		//	System.out.println("Car with id :"+c.getId()+" exited.");
			c.setExitTime(time+10);
			c.carExited(); // Set exited flag of car to true.
			return 0; // Return 0 if car exited.
		}
		else
		{
			//c.getCurrentLight().decrementCounter();
			c.path.get(c.getNextLightIndex()).addcar(c); //We added car to next traffic light.
		//	System.out.print("Car with id:"+c.getId() +" is moving to lane of New Intersection:");
		//	c.getCurrentLight().printpos();
		//	System.out.println(" ");
			return 1;// Return 1 if car did not complete its path.
		}
		
	}
	
	public int getNumberOfTurningCars()
	{
		return list1.size()+list3.size();
	}
	
	public int getLeftLaneSize()
	{
		return list1.size();
	}
	
	public ArrayList<Car> getLeftLane()
	{
		return list1;
	}
	
	public int getMiddleLaneSize()
	{
		return list2.size();
	}
	
	public ArrayList<Car> getMiddleLane()
	{
		return list2;
	}
	
	public int getRightLaneSize()
	{
		return list3.size();
	}
	
	public ArrayList<Car> getRightLane()
	{
		return list3;
	}
	
	public LightColor getCurrentLightColor()
	{
		return currentlight;
	}
	
	public int getRemainingTime()
	{
		return remainingtime;
	}
	
	public void decRemainingtime()
	{
		remainingtime--;
	}
}
