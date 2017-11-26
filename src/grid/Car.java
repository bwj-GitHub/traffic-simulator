package grid;

import java.util.ArrayList;

import events.CarUpdateEvent;
import events.Event;
import simulator.Config;

public class Car {
	private int id;
	private int nextlightindex=0; // Can be used to find the next light car needs to move to.
	private int entrytime;
	private int exittime;
	//car has 2 states, state=1 means car is moving, state=0 means car is at rest
	private int state=1;
	private int lane;
	public ArrayList<PathElement> path;
	private TrafficLight currentlight;
	//private ArrayList<TrafficLight> turns;
	private int pos; //position in lane
	private int size=5;
	private int carsgap=1;
	private Lane firstturn;
	private Lane secondturn;
	private Lane thirdturn;
	private int numofturns=0;
	private int turncount1; // When turncount is 0 it means that car should take turn now.
	private int turncount2;
	private int carspeed=10;
	private int acceleration=5;
	private int carlength=5;
	private int intersectionlength=100;
	private int carspacing=1;
	private Lane currentlane;
	private boolean exited=false;
	
	
	public  Car(int time,ArrayList<PathElement> p,int i,Config config)
	{
		entrytime=time;
		path=p;
		id=i;
		this.size=config.carsize;
		this.carsgap=config.carspacing;
		this.carspacing=config.carspacing;
		this.carspeed=config.carspeed;
		this.acceleration=config.caracceleration;
		this.carlength=config.carsize;
		this.intersectionlength=config.distrows;
	}
	
	public  Car(int time,ArrayList<PathElement> p,int i,int turns,Lane l1,Lane l2,int count,Config config)
	{
		entrytime=time;
		path=p;
		id=i;
		numofturns=turns;
		firstturn=l1;
		secondturn=l2;
		turncount1=count;
		this.size=config.carsize;
		this.carsgap=config.carspacing;
		this.carspacing=config.carspacing;
		this.carspeed=config.carspeed;
		this.acceleration=config.caracceleration;
		this.carlength=config.carsize;
		this.intersectionlength=config.distrows;
	}
	
	public  Car(int time,ArrayList<PathElement> p,int i,int turns,Lane l1,Lane l2,Lane l3,int count1,int count2,Config config)
	{
		entrytime=time;
		path=p;
		id=i;
		numofturns=turns;
		firstturn=l1;
		secondturn=l2;
		thirdturn=l3;
		turncount1=count1;
		turncount2=count2;
		this.size=config.carsize;
		this.carsgap=config.carspacing;
		this.carspacing=config.carspacing;
		this.carspeed=config.carspeed;
		this.acceleration=config.caracceleration;
		this.carlength=config.carsize;
		this.intersectionlength=config.distrows;
	}
	
	public int getTurnCount1()
	{
		return turncount1;
	}
	
	public int getTurnCount2()
	{
		return turncount2;
	}
	
	public int getAcceleration()
	{
		return acceleration;
	}
	
	public int getIntersectionLength()
	{
		return intersectionlength;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getState()
	{
		return state;
	}
	
	public void setExitTime(int time)
	{
		exittime=time;
	}
	
	public void carExited()
	{
		exited=true;
	}
	
	public boolean hasCarExited()
	{
		return exited;
	}
	
	public int getCarSize()
	{
		return carlength;
	}
	
	public int getCarSpacing()
	{
		return carspacing;
	}
	
	public int getNumOfTurns()
	{
		return numofturns;
	}
	
	public Lane getLane()
	{
		if(numofturns==1)
		{
			if(turncount1>0) return firstturn;
			else return secondturn;
		}
		else
		{
		if(turncount1>0) return firstturn;
		else if(turncount2>0) return secondturn;
		else return thirdturn;
		}
	}
	
	public void decTurnCount1()
	{
		turncount1--;
	}
	
	public void decTurnCount2()
	{
		turncount2--;
	}
	
	public void setposition(int position)
	{
		pos=position;
	}
	
	public int getPosition()
	{
		return pos;
	}
	public Event generateCarUpdateEvent(int currenttime) // Added acceleration.
	{
		int time;
		int numofcars;
		if(currentlane==grid.Lane.left) numofcars=currentlight.getLeftLaneSize();
		else if(currentlane==grid.Lane.middle) numofcars=currentlight.getMiddleLaneSize();
		else numofcars=currentlight.getRightLaneSize();
		if(numofcars==1)
		{
			if(acceleration==0)
				time=intersectionlength/carspeed;
			else
			time=(carspeed/acceleration)+(intersectionlength/carspeed);
		}
		else
		{
			int distance=intersectionlength-(((numofcars-pos)*carlength)+((numofcars-pos)*carspacing));
			if(acceleration==0)
			{
				time=distance/carspeed;
			}
			else
			{
			int s=(carspeed^2)/(2*acceleration);
			if(distance>=2*s)
			{
				time=(carspeed/acceleration)+(distance/carspeed);
			}
			else
			{
				time=(int) (2*Math.sqrt(distance/acceleration));
			}
			}
		}
		Event e=new CarUpdateEvent(this, currenttime+time);
		return e;
	}
	
	public int getTime() // Added acceleration.
	{
		int time;
		int numofcars;
		if(currentlane==grid.Lane.left) numofcars=currentlight.getLeftLaneSize();
		else if(currentlane==grid.Lane.middle) numofcars=currentlight.getMiddleLaneSize();
		else numofcars=currentlight.getRightLaneSize();
		if(numofcars==1)
		{
			if(acceleration==0)
				time=intersectionlength/carspeed;
			else
				time=(carspeed/acceleration)+(intersectionlength/carspeed);
		}
		else
		{
			int distance=intersectionlength-(((numofcars-pos)*carlength)+((numofcars-pos)*carspacing));
			if(acceleration==0)
				time=distance/carspeed;
			else
			{
			int s=(carspeed^2)/(2*acceleration);
			if(distance>=2*s)
			{
				time=(carspeed/acceleration)+(distance/carspeed);
			}
			else
			{
				time=(int) (2*Math.sqrt(distance/acceleration));
			}
			}
		}
	//	Event e=new CarUpdateEvent(this,eventtypeenum.carupdate,currenttime+time);
		return time;
	}
	
	public void setCurrentLight(TrafficLight t)
	{
		currentlight=t;
	}
	
	public TrafficLight getCurrentLight()
	{
		return currentlight;
	}
	
	public void setCurrentLane(Lane l)
	{
		currentlane=l;
	}
	
	public Lane getCurrentLane()
	{
		return currentlane;
	}
	
	public void rest()
	{
		state=0;
	}
	
	public void moving()
	{
		state=1;
	}
	
	public void incrementNextLightIndex()
	{
		nextlightindex++;
	}
	
	public int getNextLightIndex()
	{
		return nextlightindex;
	}
	
	public int getCarSpeed()
	{
		return carspeed;
	}
	
	public int getTimeInGrid()
	{
		return exittime-entrytime;
	}
}

