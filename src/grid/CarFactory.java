package grid;

import java.util.ArrayList;
import java.util.Random;

import events.CarSpawnEvent;
import events.Event;
import simulator.Config;
import events.EventQueue;



public class CarFactory {
	Config config;
	int numAvenues;
	int numStreets;

	public CarFactory(Config config) {
		this.config = config;
		this.numAvenues = config.numrows;  // BJ: Shouldn't this be cols?
		this.numStreets = config.numcol;
	}


	public Car newcar(int id, int arrivaltime, TrafficGrid trafficgrid)
	{
		// We first need to generate the path of the car.
	//	System.out.println("Number of rows and cols are :"+numavenues+","+numstreets);
		ArrayList<Intersection> entrys=new ArrayList<Intersection>();
		ArrayList<PathElement> path=new ArrayList<PathElement>();
		Lane l1 = null;
		Lane l2=null;
		Lane l3=null;
		TrafficLight turn1;
		int turncounter1=0;
		int turncounter2=0;
		//int entrypoints=numavenues+numstreets;
		// We are going to find entry intersections list.
		for(int i=0;i<numAvenues;i++)
		{
			if(i%2==0)
			
				entrys.add(trafficgrid.getIntersection(i,numStreets-1));
			
			else
			
				entrys.add(trafficgrid.getIntersection(i, 0));
			
		}
		
		for(int i=0;i<numStreets;i++)
		{
			if(i%2==0)
			
				entrys.add(trafficgrid.getIntersection(0,i));
			
			else
				
				entrys.add(trafficgrid.getIntersection(numAvenues-1, i));
		
		}
		//We found entry points , Now we will generate paths randomly with 0/1/2 turns.
		Random r=new Random();
		int numofturns=r.nextInt(3);
		 numofturns=0; //For convoy scheduling algorithm.
		Intersection e=entrys.get(r.nextInt(numAvenues+numStreets));
		if (numofturns==0)
		{
			l1=Lane.middle;
			if(e.getypos()==0 && e.getxpos()!=0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight1());
				path.add(new PathElement(i,j,1));
				j++;
				while(j<numStreets)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight1());
					path.add(new PathElement(i,j,1));
					j++;
				}
			}
			
			else if(e.getypos()==0&&e.getxpos()==0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i++;
				while(i<numAvenues)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i++;
				}
			}
			else if (e.getypos()==numStreets-1 && e.getxpos()!=numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight1());
				path.add(new PathElement(i,j,1));
				j--;
				while(j>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight1());
					path.add(new PathElement(i,j,1));
					j--;
				}
			}
			else if (e.getypos()!=0 && e.getxpos()==0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i++;
				while(i<numAvenues)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i++;
				}
			}
			else if (e.getypos()==numStreets-1 && e.getxpos()==numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i--;
				while(i>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i--;
				}
			}
			else if(e.getypos()!=0 && e.getxpos()==numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i--;
				while(i>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i--;
				}
			}
		}
		
		else if(numofturns==1)
		{
			l2=Lane.middle;
			int offset=r.nextInt(numAvenues); //Randomly determine an intersection to turn.
		//	System.out.println("Offset value is :"+offset);
			if(e.getypos()==0 && e.getxpos()!=0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight1());
				path.add(new PathElement(i,j,1));
				j++;
				offset--;
				while(j<numStreets && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight1());
					path.add(new PathElement(i,j,1));
					j++;
					offset--;
				}
				turncounter1=path.size();
				j--;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
				{
					l1=Lane.right;
					i++;
					while(i<numAvenues)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight2());
						path.add(new PathElement(i,j,2));
						i++;
					}
				}
				
				else
				{
					l1=Lane.left;
					i--;
					while(i>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight2());
						path.add(new PathElement(i,j,2));
						i--;
					}
				}
			}
			
			else if(e.getypos()==0&&e.getxpos()==0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i++;
				offset--;
				while(i<numAvenues && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i++;
					offset--;
				}
				turncounter1=path.size();
				i--;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					l1=Lane.left;
					j++;
					while(j<numStreets)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
					}
				}
				
				else
				{
					l1=Lane.right;
					j--;
					while(j>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
					}
				}
				
			}
			else if (e.getypos()==numStreets-1 && e.getxpos()!=numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight1());
				path.add(new PathElement(i,j,1));
				j--;
				offset--;
				while(j>=0 && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight1());
					path.add(new PathElement(i,j,1));
					j--;
					offset--;
				}
				turncounter1=path.size();
				j++;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
				{
					l1=Lane.left;
					i++;
					while(i<numAvenues)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight2());
						path.add(new PathElement(i,j,2));
						i++;
					}
				}
				
				else
				{
					l1=Lane.right;
					i--;
					while(i>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight2());
						path.add(new PathElement(i,j,2));
						i--;
					}
				}
			}
			else if (e.getypos()!=0 && e.getxpos()==0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i++;
				offset--;
				while(i<numAvenues && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i++;
					offset--;
				}
				turncounter1=path.size();
				i--;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					l1=Lane.left;
					j++;
					while(j<numStreets)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
					}
				}
				
				else
				{
					l1=Lane.right;
					j--;
					while(j>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
					}
				}
			}
			else if (e.getypos()==numStreets-1 && e.getxpos()==numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i--;
				offset--;
				while(i>=0 && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i--;
					offset--;
				}
				turncounter1=path.size();
				i++;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					l1=Lane.right;
					j++;
					while(j<numStreets)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
					}
				}
				
				else
				{
					l1=Lane.left;
					j--;
					while(j>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
					}
				}
			}
			else if(e.getypos()!=0 && e.getxpos()==numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i--;
				offset--;
				while(i>=0 && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i--;
					offset--;
				}
				turncounter1=path.size();
				i++;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					l1=Lane.right;
					j++;
					while(j<numStreets)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
					}
				}
				
				else
				{
					l1=Lane.left;
					j--;
					while(j>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
					}
				}
			}
			
		}
		
		else
		{
			l3=Lane.middle;
			int offset=r.nextInt(numAvenues); //Randomly determine an intersection to turn.
			int offset1;
		//	System.out.println("Offset value is :"+offset);
			if(e.getypos()==0 && e.getxpos()!=0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight1());
				path.add(new PathElement(i,j,1));
				j++;
				offset--;
				while(j<numStreets && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight1());
					path.add(new PathElement(i,j,1));
					j++;
					offset--;
				}
				turncounter1=path.size();
				j--;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
				{
					if(numAvenues-i-1==0)
						offset1=0;
					else
					offset1=r.nextInt(numAvenues-i-1);
					l1=Lane.right;
					i++;
					while(i<numAvenues&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight2());
						path.add(new PathElement(i,j,2));
						i++;
						offset1--;
					}
					turncounter2=path.size();
					i--;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
					{
						l2=Lane.left;
						j++;
						while(j<numStreets)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j++;
						}
					}
					else
					{
						l2=Lane.right;
						j--;
						while(j>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j--;
						}
					}
				}
				
				else
				{
					offset1=r.nextInt(i);
					l1=Lane.left;
					i--;
					while(i>=0&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight2());
						path.add(new PathElement(i,j,2));
						i--;
						offset1--;
					}
					turncounter2=path.size();
					i++;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
					{
						l2=Lane.right;
						j++;
						while(j<numStreets)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j++;
						}
					}
					else
					{
						l2=Lane.left;
						j--;
						while(j>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j--;
						}
					}
				}
			}
			
			else if(e.getypos()==0&&e.getxpos()==0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i++;
				offset--;
				while(i<numAvenues && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i++;
					offset--;
				}
				turncounter1=path.size();
				i--;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					offset1=r.nextInt(numStreets-1);
					l1=Lane.left;
					j++;
					while(j<numStreets&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
						offset1--;
					}
					turncounter2=path.size();
					j--;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.right;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.left;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
				}
				
				else
				{
					offset1=r.nextInt(numStreets-1);
					l1=Lane.right;
					j--;
					while(j>=0&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
						offset1--;
					}
				}
				
			}
			else if (e.getypos()==numStreets-1 && e.getxpos()!=numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight1());
				path.add(new PathElement(i,j,1));
				j--;
				offset--;
				while(j>=0 && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight1());
					path.add(new PathElement(i,j,1));
					j--;
					offset--;
				}
				turncounter1=path.size();
				j++;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
				{
					offset1=r.nextInt(numAvenues-i-1);
					l1=Lane.left;
					i++;
					while(i<numAvenues&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight2());
						path.add(new PathElement(i,j,2));
						i++;
					}
					turncounter2=path.size();
					i--;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
					{
						l2=Lane.left;
						j++;
						while(j<numStreets)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j++;
						}
					}
					else
					{
						l2=Lane.right;
						j--;
						while(j>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j--;
						}
					}
				}
				
				else
				{
					if(i==0)
						offset1=0;
						else
					offset1=r.nextInt(i);
					l1=Lane.right;
					i--;
					while(i>=0&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight2());
						path.add(new PathElement(i,j,2));
						i--;
						offset1--;
					}
					turncounter2=path.size();
					i++;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
					{
						l2=Lane.right;
						j++;
						while(j<numStreets)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j++;
						}
					}
					else
					{
						l2=Lane.left;
						j--;
						while(j>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight1());
							path.add(new PathElement(i,j,1));
							j--;
						}
					}
				}
			}
			else if (e.getypos()!=0 && e.getxpos()==0)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i++;
				offset--;
				while(i<numAvenues && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i++;
					offset--;
				}
				turncounter1=path.size();
				i--;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					offset1=r.nextInt(numStreets-j-1);
					l1=Lane.left;
					j++;
					while(j<numStreets&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
						offset1--;
					}
					turncounter2=path.size();
					j--;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.right;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.left;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
				}
				
				else
				{
					l1=Lane.right;
					j--;
					while(j>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
					}
					turncounter2=path.size();
					j++;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.left;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.right;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
				}
			}
			else if (e.getypos()==numStreets-1 && e.getxpos()==numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i--;
				offset--;
				while(i>=0 && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i--;
					offset--;
				}
				turncounter1=path.size();
				i++;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					if(numStreets-j-1==0)
						offset1=0;
					else
					offset1=r.nextInt(numStreets-j-1);
					l1=Lane.right;
					j++;
					while(j<numStreets&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
						offset1--;
					}
					turncounter2=path.size();
					j--;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.right;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.left;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
				
				}
				
				else
				{
					offset1=r.nextInt(j);
					l1=Lane.left;
					j--;
					while(j>=0&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
						offset1--;
					}
					turncounter2=path.size();
					j++;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.left;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.right;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
					
				}
			}
			else if(e.getypos()!=0 && e.getxpos()==numAvenues-1)
			{
				int i=e.getxpos();
				int j=e.getypos();
				//path.add(e.getlight2());
				path.add(new PathElement(i,j,2));
				i--;
				offset--;
				while(i>=0 && offset>=0)
				{
					//path.add(trafficgrid.getIntersection(i, j).getlight2());
					path.add(new PathElement(i,j,2));
					i--;
					offset--;
				}
				turncounter1=path.size();
				i++;
				if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.we)
				{
					offset1=r.nextInt(numStreets-j-1);
					l1=Lane.right;
					j++;
					while(j<numStreets&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i, j).getlight1());
						path.add(new PathElement(i,j,1));
						j++;
						offset1--;
					}
					turncounter2=path.size();
					j--;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.right;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.left;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
				}
				
				else
				{
					offset1=r.nextInt(j);
					l1=Lane.left;
					j--;
					while(j>=0&&offset1>=0)
					{
						//path.add(trafficgrid.getIntersection(i,j).getlight1());
						path.add(new PathElement(i,j,1));
						j--;
						offset1--;
					}
					turncounter2=path.size();
					j++;
					if(path.get(path.size()-1).getotherlighttrafficdirection()==TrafficDirection.ns)
					{
						l2=Lane.left;
						i++;
						while(i<numAvenues)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i++;
						}
					}
					else
					{
						l2=Lane.right;
						i--;
						while(i>=0)
						{
							//path.add(trafficgrid.getIntersection(i, j).getlight2());
							path.add(new PathElement(i,j,2));
							i--;
						}
					}
				}
			}
		}
		
		// TODO : Write code for finding path when number of turns=2.
		//
		//
		
		// We have found the path , Now we will initialize the car.
		if (numofturns==0)
		{	
			Car c=new Car(arrivaltime,path,id,config);
			return c;
		}
		else if(numofturns==1)
		{
			Car c=new Car(arrivaltime,path,id,numofturns,l1,l2,turncounter1,config);
			return c;
		}
		else //Modify code for numofturns=2.
		{
		Car c=new Car(arrivaltime,path,id,numofturns,l1,l2,l3,turncounter1,turncounter2,config);
		return c;
		}
	}
	
	public CarSpawnEvent[] generateCarSpawnEvent(Config config)
	{
		// using negative exponential random number generator we generate car creation events and add them to the eventqueue.
		
		// Here I'm using random numbers to generate car spawn events.
		int n=config.numcars;
		float time=1;
		Random r=new Random();  // TODO, BJ: we need to be able to initialize with seed
		InterarrivalTimeGenerator rate=new InterarrivalTimeGenerator(config.lambda,r);
		CarSpawnEvent[] carSpawnEvents = new CarSpawnEvent[n];
		for(int i=0; i<n; i++)
		{
			time+=rate.getNextArrivalTime();
			carSpawnEvents[i] = new CarSpawnEvent((int)time);
		}
		System.out.println("Genetared number of car spawn events are :" + n);
		return carSpawnEvents;
	}
}
