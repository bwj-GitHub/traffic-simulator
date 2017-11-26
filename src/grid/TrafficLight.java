package grid;

import java.util.ArrayList;

import events.Event;
import simulator.Config;

public class TrafficLight {
	
	private Config config;
	private int redtime=8;
	private int greentime=5;
	private int yellowtime=3;
	private int threshold=5000;//Number of cars which turn light to green for traffic light.
	private int counter=0; // Number of cars at intersection at that current traffic light. 
	private int lanelimit=10;
	private int threshold_coordinated=2;//Number of cars leaving lane which turn next traffic light to green.
	private int xpos;
	private int ypos;
	public boolean s1=false;
	public boolean s2=false;
	private int llcarstomove=0;
	private int mlcarstomove=0;
	private int rlcarstomove=0;
	private int otherlightcounter=0;
	private boolean flag=false;
	private boolean flag_coordinated=false; // For middle lane.
	private boolean flag_coordinated1=false; // For right/left lane.
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
	
	public int getcurrentposition(Car c)
	{
		int n;
		if(c.getCurrentLane()==Lane.left)n=list1.indexOf(c)+1;
		else if(c.getCurrentLane()==Lane.middle)n=list2.indexOf(c)+1;
		else n=list3.indexOf(c)+1;
		return n;
	}
	
	public void setremaininglighttime(int n)
	{
		remainingtime=n;
	}
	
	public int getLaneLimit()
	{
		return lanelimit;
	}
	
	public int getThreshold_Coordinated()
	{
		return threshold_coordinated;
	}
	
	public boolean getFlag_Coordinated()
	{
		return flag_coordinated;
	}
	
	public void setFlag_Coordinated()
	{
		flag_coordinated=true;
	}
	
	public void unsetFlag_Coordinated()
	{
		flag_coordinated=false;
	}
	
	public boolean getFlag_Coordinated1()
	{
		return flag_coordinated1;
	}
	
	public void setFlag_Coordinated1()
	{
		flag_coordinated1=true;
	}
	
	public void unsetFlag_Coordinated1()
	{
		flag_coordinated1=false;
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
	
	public void setlighttogreenevent(int carspeed,int carlength,int carspacing,int algorithm)
	{
		currentlight=LightColor.green;
		remainingtime=greentime;
		TrafficLight light1=this;
		int num1=light1.getLeftLaneSize();
		int num2=light1.getMiddleLaneSize();
		int num3=light1.getRightLaneSize();
		int remainingtime=light1.getRemainingTime();
		ArrayList<Car> leftlane=light1.getLeftLane();
		ArrayList<Car> middlelane=light1.getMiddleLane();
		ArrayList<Car> rightlane=light1.getRightLane();
		int numcars=((remainingtime*carspeed)+carspacing)/(carlength+carspacing);
		
		if(algorithm==4)
		{
			num2=0;
			
			for(Car car:middlelane)
			{
				if(car.getState()==0)
					num2++;
			}
			
			if(num2>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated();
			}
			
			if(num2<numcars)
			{
				light1.setMLCarsToMove(num2);
			}
			else
			{
			//	System.out.println("Extending green light");
				light1.setMLCarsToMove(num2);
				int time;
				int x=((num2*(carlength+carspacing))-carspacing);
				if(x%carspeed==0)
					time=x/carspeed;
				else
					time=(x/carspeed)+1;
				//Extending current light time.
				light1.setremaininglighttime(time);
				light1.getOtherLight().setremaininglighttime(light1.getOtherLight().getYellowTime()+time);
			}
		}
		
		else
		{
		if(numcars<num1)
		{
			light1.setLLCarsToMove(numcars);
			if(numcars>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated1();
			}
		}
		else
		{
			light1.setLLCarsToMove(num1);
			if(num1>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated1();
			}
		}
		
		if(numcars<num2)
		{
			light1.setMLCarsToMove(numcars);
			if(numcars>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated();
			}
		}
		else
		{
			light1.setMLCarsToMove(num2);
			if(num2>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated();
			}
		}
		
		if(numcars<num3)
		{
			light1.setRLCarsToMove(numcars);
			if(numcars>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated1();
			}
		}
		else
		{
			light1.setRLCarsToMove(num3);
			if(num3>=light1.getThreshold_Coordinated())
			{
				light1.setFlag_Coordinated1();
			}
		}
		}
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
		//	System.out.print("At time :"+time+" Car with id:"+c.getId() +" is moving to lane of New Intersection:");
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
