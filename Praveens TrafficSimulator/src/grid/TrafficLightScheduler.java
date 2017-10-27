package grid;

import java.util.ArrayList;

import events.EventQueue;

public class TrafficLightScheduler {
	
	// We handle traffic light events here such as traffic light change when something happens.

	public void UpdateTrafficLights(TrafficGrid grid,int numavenues,int numstreets,int carlength,int carspacing,int carspeed,int currenttime,EventQueue eventqueue)
	{ // Regular traffic light updates.
		//System.out.println("We are in updatetrafficlights function");
		for(int i=0;i<numavenues;i++)
			for(int j=0;j<numstreets;j++)
			{
			//	System.out.println("Inside loop with i"+i+" j "+j);
				// Three cases : Light is red,green,yellow.
				// We need to do additional things when light changes from one color to another.
				TrafficLight light1=grid.getIntersection(i, j).getlight1();
				TrafficLight light2=grid.getIntersection(i, j).getlight2();
				if(light1.getCurrentLightColor()==lightcolor.green)
				{
					light1.decRemainingtime();
					if(light1.getRemainingTime()==0) // Change light to yellow.
					{
						// Set light to yellow and move the front car of each lane to next traffic light.
					//	System.out.println("Changing light1 to yellow at time:"+currenttime);
						light1.setlighttoyellow();
						ArrayList<Car> leftlane=light1.getLeftLane();
						ArrayList<Car> middlelane=light1.getMiddleLane();
						ArrayList<Car> rightlane=light1.getRightLane();
						if(leftlane.size()!=0)
						{
						Car c1=leftlane.get(0);
						if(c1.getState()==0)
						{
						int check=c1.getCurrentLight().removecar(c1, currenttime);
						c1.moving();
						if(check==1)
						eventqueue.add(c1.generateCarUpdateEvent(currenttime));
						}
						}
						if(middlelane.size()!=0)
						{
						Car c2=middlelane.get(0);
						if(c2.getState()==0)
						{
						int check=c2.getCurrentLight().removecar(c2, currenttime);
						c2.moving();
						if(check==1)
						eventqueue.add(c2.generateCarUpdateEvent(currenttime));
						}
						}
						if(rightlane.size()!=0)
						{
						Car c3=rightlane.get(0);
						if(c3.getState()==0)
						{
						int check=c3.getCurrentLight().removecar(c3, currenttime);
						c3.moving();
						if(check==1)
						eventqueue.add(c3.generateCarUpdateEvent(currenttime));
						}
						}
					}
				}
				else if(light1.getCurrentLightColor()==lightcolor.yellow)
				{
					light1.decRemainingtime();
					if(light1.getRemainingTime()==0) // Change light to red.
					{
						// Set light to red.
					//	System.out.println("Changing light1 to red at time:"+currenttime);
						light1.setlighttored();
					}
					
				}
				else // Light1 color is red.
				{
					light1.decRemainingtime();
					if(light1.getRemainingTime()==0) // Change light to green.
					{
						//Set light to green and move cars.
					//	System.out.println("Changing light1 to green at time:"+currenttime);
						light1.setlighttogreen();
						int num1=light1.getLeftLaneSize();
						int num2=light1.getMiddleLaneSize();
						int num3=light1.getRightLaneSize();
						int remainingtime=light1.getRemainingTime();
						ArrayList<Car> leftlane=light1.getLeftLane();
						ArrayList<Car> middlelane=light1.getMiddleLane();
						ArrayList<Car> rightlane=light1.getRightLane();
						int numcars=((remainingtime*carspeed)+carspacing)/(carlength+carspacing);
						if(numcars<num1)
						{
							int index=0;
							for(int temp=0;temp<numcars;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								
								Car c=light1.getLeftLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0; // If a car in queue is moving we should not process it and in next iteration we should retrieve next index car,otherwise retrieve car at index 0.	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						else
						{
							int index=0;
							for(int temp=0;temp<num1;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light1.getLeftLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						
						if(numcars<num2)
						{
							int index=0;
							for(int temp=0;temp<numcars;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light1.getMiddleLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						else
						{
							int index=0;
							for(int temp=0;temp<num2;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light1.getMiddleLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						
						if(numcars<num3)
						{
							int  index=0;
							for(int temp=0;temp<numcars;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light1.getRightLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						else
						{
							int index=0;
							for(int temp=0;temp<num3;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light1.getRightLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
					}
				}
				
				if(light2.getCurrentLightColor()==lightcolor.green)
				{
					light2.decRemainingtime();
					if(light2.getRemainingTime()==0) // Change light to yellow.
					{
						// Set light to yellow and move the front car of each lane to next traffic light.
						light2.setlighttoyellow();
						ArrayList<Car> leftlane=light2.getLeftLane();
						ArrayList<Car> middlelane=light2.getMiddleLane();
						ArrayList<Car> rightlane=light2.getRightLane();
						if(leftlane.size()!=0)
						{
						Car c1=light2.getLeftLane().get(0);
						if(c1.getState()==0)
						{
						int check=c1.getCurrentLight().removecar(c1, currenttime);
						c1.moving();
						if(check==1)
						eventqueue.add(c1.generateCarUpdateEvent(currenttime));
						}
						}
						if(middlelane.size()!=0)
						{
						Car c2=middlelane.get(0);
						if(c2.getState()==0)
						{
						int check=c2.getCurrentLight().removecar(c2, currenttime);
						c2.moving();
						if(check==1)
						eventqueue.add(c2.generateCarUpdateEvent(currenttime));
						}
						}
						if(rightlane.size()!=0)
						{
						Car c3=rightlane.get(0);
						if(c3.getState()==0)
						{
						int check=c3.getCurrentLight().removecar(c3, currenttime);
						c3.moving();
						if(check==1)
						eventqueue.add(c3.generateCarUpdateEvent(currenttime));
						
						}
						}
					}
				}
				else if(light2.getCurrentLightColor()==lightcolor.yellow)
				{
					light2.decRemainingtime();
					if(light2.getRemainingTime()==0) // Change light to red.
					{
						light2.setlighttored();
					}
				}
				else // Light2 color is red.
				{
					light2.decRemainingtime();
					if(light2.getRemainingTime()==0) //Change light to green.
					{
						light2.setlighttogreen();
						int num1=light2.getLeftLaneSize();
						int num2=light2.getMiddleLaneSize();
						int num3=light2.getRightLaneSize();
						int remainingtime=light2.getRemainingTime();
						ArrayList<Car> leftlane=light2.getLeftLane();
						ArrayList<Car> middlelane=light2.getMiddleLane();
						ArrayList<Car> rightlane=light2.getRightLane();
						int numcars=((remainingtime*carspeed)+carspacing)/(carlength+carspacing);
						if(numcars<num1)
						{
							int index=0;
							for(int temp=0;temp<numcars;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light2.getLeftLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						else
						{
							int index=0;
							for(int temp=0;temp<num1;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light2.getLeftLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						
						if(numcars<num2)
						{
							int index=0;
							for(int temp=0;temp<numcars;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light2.getMiddleLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						else
						{
							int index=0;
							for(int temp=0;temp<num2;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light2.getMiddleLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						
						if(numcars<num3)
						{
							int index=0;
							for(int temp=0;temp<numcars;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light2.getRightLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
							}
						}
						else
						{
							int index=0;
							for(int temp=0;temp<num3;temp++)
							{
								int t=((carlength+carspacing)*temp)/carspeed;
								Car c=light2.getRightLane().get(index);
								index++;
								if(c.getState()==0)
								{
								index=0;	
								int check=c.getCurrentLight().removecar(c, currenttime+t);
								c.moving();
								if(check==1)
								eventqueue.add(c.generateCarUpdateEvent(currenttime+t));
								}
								
							}
						}
					}
				}
			}
	}
}
