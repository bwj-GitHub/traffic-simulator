package grid;

import java.util.Random;

public class Intersection {

	private Config config;
	private int numavenues;
	private int numstreets;
	private TrafficLight light1,light2;
	private trafficdirection xdirection;
	private trafficdirection ydirection;
	private int xpos,ypos;
	//methods
	public Intersection(int x,int y,int avenues,int streets,Config config)
	{
		this.config=config;
		xpos=x;
		ypos=y;
		numavenues=avenues;
		numstreets=streets;
		if(xpos%2==0) xdirection=trafficdirection.ew;
		else xdirection=trafficdirection.we;
		if(ypos%2==0) ydirection=trafficdirection.ns;
		else ydirection=trafficdirection.sn;
		light1=new TrafficLight(config);
		light2=new TrafficLight(config);
		light1.setOtherLight(light2);
		light2.setOtherLight(light1);
		light1.setdirection(xdirection);
		light2.setdirection(ydirection);
		light1.setpos(xpos, ypos);
		light2.setpos(xpos, ypos);
		Random rand=new Random();
		light1.setlighttogreen();
		light2.setlighttogreen();
		//we randomly set one of light at this intersection to green
		/*
		if(rand.nextInt(2)==0)
			{
			light1.setlighttogreen();
			light2.setlighttored();
			}
		else
		{
			light1.setlighttored();
			light2.setlighttogreen();
		}
		*/
	}
	
	public int getxpos()
	{
		return xpos; 
	}
	
	public int getypos()
	{
		return ypos;
	}
	
	public TrafficLight getlight1()
	{
		return light1;
	}
	
	public TrafficLight getlight2()
	{
		return light2;
	}
	
	
	
}
