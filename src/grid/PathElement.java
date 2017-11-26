package grid;

public class PathElement {
	
	private int xpos; 
	private int ypos;
	private int lightnumber; // 1 indicates x-direction light, 2 indicates y-direction light.
	
	public PathElement(int xpos,int ypos,int lightnumber)
	{
		this.xpos=xpos;
		this.ypos=ypos;
		this.lightnumber=lightnumber;
	}
	
	public int getxpos()
	{
		return xpos;
	}
	
	public int getypos()
	{
		return ypos;
	}
	
	public int getlightnumber()
	{
		return lightnumber;
	}
	
	public TrafficDirection getotherlighttrafficdirection()
	{
		if(lightnumber==1) 
		{
			if(ypos%2==0) return TrafficDirection.ns;
			else return TrafficDirection.sn;
		}
		else
		{
			if(xpos%2==0) return TrafficDirection.ew;
			else return TrafficDirection.we;
		}
	}
	
	public TrafficLight gettrafficlight(TrafficGrid grid)
	{
		if(lightnumber==1)
		{
		return	grid.getIntersection(xpos, ypos).getlight1();
		}
		else
		{
		return	grid.getIntersection(xpos, ypos).getlight2();
		}
	}

}
