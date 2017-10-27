package grid;

import java.util.ArrayList;

public class TrafficLight {
	
	private Config config;
	private int redtime=5;
	private int greentime=5;
	private int yellowtime=3;
	private int xpos;
	private int ypos;
	private lightcolor currentlight;
	private int remainingtime;
	private trafficdirection direction;
	private TrafficLight otherlight;
	private ArrayList<Car> list1=new ArrayList<Car>();
	private ArrayList<Car> list2=new ArrayList<Car>();
	private ArrayList<Car> list3=new ArrayList<Car>();
	
	//methods.
	
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
		currentlight=lightcolor.green;
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
	public void setlighttored()
	{
		currentlight=lightcolor.red;
		remainingtime=redtime;
	}
	
	public void setlighttoyellow()
	{
		currentlight=lightcolor.yellow;
		remainingtime=yellowtime;
	}
	
	public void setdirection(trafficdirection d)
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
	
	public trafficdirection getTrafficDirection()
	{
		return direction;
	}
	
	public void addcar(Car c)  // Adds car to respective lane for that traffic light.
	{
		if (c.getNumOfTurns()==0)
		{
			list2.add(c);
			c.setposition(list2.size());
			c.setCurrentLane(lane.middle);
		}	
		else if(c.getNumOfTurns()==1)
		{
			if(c.getLane()==lane.left)
			{ 
				list1.add(c);
				c.setposition(list1.size());
				c.setCurrentLane(lane.left);
			}
			else 
			{	
				list3.add(c);
				c.setposition(list3.size());
				c.setCurrentLane(lane.right);
			}
				c.decTurnCount1();
		}
		else
		{
			if(c.getLane()==lane.left)
			{ 
				list1.add(c);
				c.setposition(list1.size());
				c.setCurrentLane(lane.left);
			}
			else 
			{
				list3.add(c);
				c.setposition(list3.size());
				c.setCurrentLane(lane.right);
			}
			c.decTurnCount1();
			c.decTurnCount2();
		}
		c.setCurrentLight(this);
		c.incrementNextLightIndex();
	}
	
	public int removecar(Car c,int time)
	{
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
			System.out.println("Car with id :"+c.getId()+" exited.");
			c.setExitTime(time+10);
			c.carExited(); // Set exited flag of car to true.
			return 0; // Return 0 if car exited.
		}
		else
		{
		
			c.path.get(c.getNextLightIndex()).addcar(c); //We added car to next traffic light.
			System.out.print("Car with id:"+c.getId() +" is moving to lane of New Intersection:");
			c.getCurrentLight().printpos();
			System.out.println(" ");
			return 1;// Return 1 if car did not complete its path.
		}
		
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
	
	public lightcolor getCurrentLightColor()
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
