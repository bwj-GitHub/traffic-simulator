package grid;

import java.util.ArrayList;

import events.EventQueue;
import events.LightEvent;

public class TrafficLightScheduler {
	// We handle traffic light events here such as traffic light change when something happens.

	// TODO, BJ: This should be broken into several smaller methods
	// TODO, BJ: This should NOT be this tightly coupled with the grid
	public void UpdateTrafficLights(TrafficGrid grid,int numavenues,int numstreets,int carlength,int carspacing,int carspeed,int currenttime,EventQueue eventqueue,int ccheck)
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
				light1.unsetFlag();
				light2.unsetFlag();
			//	boolean s1=false;
			//	boolean s2=false;
				if(light1.getCurrentLightColor()==LightColor.green)
				{
					light1.decRemainingtime();
					//if(light1.getLLCarsToMove()!=0 || light1.getMLCarsToMove()!=0 || light1.getRLCarsToMove()!=0)
					//System.out.println("light 1 ll,ml,rl cars to move:"+light1.getLLCarsToMove()+light1.getMLCarsToMove()+light1.getRLCarsToMove()+" time is:"+currenttime);
					if(light1.getLLCarsToMove()>0)
					{
						if(light1.getLeftLaneSize()!=0)
						{
						Car c=light1.getLeftLane().get(0);
						if(c.getState()==0)
						{
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								//System.out.println(" Moving car from traffic light update , Time is :"+ currenttime);
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
								{
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
									if(light1.getFlag_Coordinated1()==true || (light1.s1==true && c.getCurrentLight().getTrafficDirection()!=light1.getTrafficDirection()))
									{
										//Creating a traffic light update event for next light of cars path.
										if(c.getCurrentLight().getTrafficDirection()==light1.getTrafficDirection())
											light1.s1=true;
									//	{	
										eventqueue.add(new LightEvent(c.getCurrentLight(), currenttime+c.getTime()));
										//if(light1.getRemainingTime()!=4 && (c.getCurrentLight().getTrafficDirection()==light1.getTrafficDirection()))
										light1.unsetFlag_Coordinated1();
										
										if(light1.s1==true&&c.getCurrentLight().getTrafficDirection()!=light1.getTrafficDirection())
											light1.s1=false;
										
									//	}
									}
								}
							}
						}
						light1.decLLCarsToMove();
						}
					}
					
					if(light1.getMLCarsToMove()>0)
					{
						if(light1.getMiddleLaneSize()!=0)
						{
						Car c=light1.getMiddleLane().get(0);
						if(c.getState()==0)
						{
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light update , Time is :"+ currenttime);
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
								if(light1.getFlag_Coordinated()==true)
								{
									//Creating a traffic light update event for next light of cars path.
									eventqueue.add(new LightEvent(c.getCurrentLight(), currenttime+c.getTime()));
									light1.unsetFlag_Coordinated();
								}
							}
						}
						light1.decMLCarsToMove();
						}
					}
					
					if(light1.getRLCarsToMove()>0)
					{
						if(light1.getRightLaneSize()!=0)
						{
						Car c=light1.getRightLane().get(0);
						if(c.getState()==0)
						{
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light update , Time is :"+ currenttime);
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
								if(light1.getFlag_Coordinated1()==true || (light1.s1==true && c.getCurrentLight().getTrafficDirection()!=light1.getTrafficDirection()))
								{
									//Creating a traffic light update event for next light of cars path.
								//	if(c.getCurrentLight().getTrafficDirection()!=light1.getTrafficDirection())
									if(c.getCurrentLight().getTrafficDirection()==light1.getTrafficDirection())
										light1.s1=true;
									eventqueue.add(new LightEvent(c.getCurrentLight(), currenttime+c.getTime()));
									light1.unsetFlag_Coordinated1();
									if(light1.s1==true&&c.getCurrentLight().getTrafficDirection()!=light1.getTrafficDirection())
										light1.s1=false;
								}
							}
						}
						light1.decRLCarsToMove();
						}
					}
					// We need to move cars from front of each lane.
					
					if(light1.getRemainingTime()==0) // Change light to yellow.
					{
						// Set light to yellow and move the front car of each lane to next traffic light.
					//	System.out.println("Changing light1 to yellow at time:"+currenttime);
						light1.setlighttoyellow();
						light1.s1=false;
						light1.s2=false;
					//	light1.unsetFlag_Coordinated1();
						
					}
				}
				else if(light1.getCurrentLightColor()==LightColor.yellow)
				{
					light1.decRemainingtime();
					if(light1.getRemainingTime()==light1.getYellowTime()-1)
					{
						ArrayList<Car> leftlane=light1.getLeftLane();
						ArrayList<Car> middlelane=light1.getMiddleLane();
						ArrayList<Car> rightlane=light1.getRightLane();
						if(leftlane.size()!=0)
						{
						Car c1=leftlane.get(0);
						if(c1.getState()==0)
						{
							if( c1.getNextLightIndex()==c1.path.size() ||(c1.getCurrentLane()==Lane.middle && c1.path.get(c1.getNextLightIndex()).getMiddleLaneSize()<=c1.getCurrentLight().getLaneLimit()) || ( c1.getCurrentLane()!=Lane.middle &&(c1.path.get(c1.getNextLightIndex()).getNumberOfTurningCars()<=c1.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light yellow update , Time is :"+ currenttime);
								int check=c1.getCurrentLight().removecar(c1, currenttime);
								c1.moving();
								if(check==1) //If car is not exiting grid.
									eventqueue.add(c1.generateCarUpdateEvent(currenttime));
							}
						}
						}
						if(middlelane.size()!=0)
						{
						Car c2=middlelane.get(0);
						if(c2.getState()==0)
						{
							if( c2.getNextLightIndex()==c2.path.size() ||(c2.getCurrentLane()==Lane.middle && c2.path.get(c2.getNextLightIndex()).getMiddleLaneSize()<=c2.getCurrentLight().getLaneLimit()) || ( c2.getCurrentLane()!=Lane.middle &&(c2.path.get(c2.getNextLightIndex()).getNumberOfTurningCars()<=c2.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light yellow update , Time is :"+ currenttime);
								int check=c2.getCurrentLight().removecar(c2, currenttime);
								c2.moving();
								if(check==1)
									eventqueue.add(c2.generateCarUpdateEvent(currenttime));
							}
						}
						}
						if(rightlane.size()!=0)
						{
						Car c3=rightlane.get(0);
						if(c3.getState()==0)
						{
							if( c3.getNextLightIndex()==c3.path.size() ||(c3.getCurrentLane()==Lane.middle && c3.path.get(c3.getNextLightIndex()).getMiddleLaneSize()<=c3.getCurrentLight().getLaneLimit()) || ( c3.getCurrentLane()!=Lane.middle &&(c3.path.get(c3.getNextLightIndex()).getNumberOfTurningCars()<=c3.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light yellow update , Time is :"+ currenttime);
								int check=c3.getCurrentLight().removecar(c3, currenttime);
								c3.moving();
								if(check==1)
									eventqueue.add(c3.generateCarUpdateEvent(currenttime));
							}
						}
						}
					}
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
						//if(currenttime==17)	
						//System.out.println("Changing light1 to green at time:"+currenttime);
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
				
				if(light2.getCurrentLightColor()==LightColor.green)
				{
					light2.decRemainingtime();
					if(light2.getLLCarsToMove()!=0 || light2.getMLCarsToMove()!=0 || light2.getRLCarsToMove()!=0)
				//	System.out.println("light 2 ll,ml,rl cars to move:"+light2.getLLCarsToMove()+light2.getMLCarsToMove()+light2.getRLCarsToMove()+" time is:"+currenttime);
					if(light2.getLLCarsToMove()>0)
					{
						if(light2.getLeftLaneSize()!=0)
						{
						Car c=light2.getLeftLane().get(0);
						if(c.getState()==0)
						{	
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
						//		System.out.println(" Moving car from traffic light update , Time is :"+ currenttime);
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
								if(light2.getFlag_Coordinated1()==true || (light2.s1==true && c.getCurrentLight().getTrafficDirection()!=light2.getTrafficDirection()))
								{
									//Creating a traffic light update event for next light of cars path.
									//if(c.getCurrentLight().getTrafficDirection()!=light2.getTrafficDirection())
									if(c.getCurrentLight().getTrafficDirection()==light2.getTrafficDirection())
										light2.s1=true;
									eventqueue.add(new LightEvent(c.getCurrentLight(), currenttime+c.getTime()));
									light2.unsetFlag_Coordinated1();
									if(light2.s1==true&&c.getCurrentLight().getTrafficDirection()!=light2.getTrafficDirection())
										light2.s1=false;
								}
							}
						}
						light2.decLLCarsToMove();
						}
					}
					
					if(light2.getMLCarsToMove()>0)
					{
						if(light2.getMiddleLaneSize()!=0)
						{
						Car c=light2.getMiddleLane().get(0);
						if(c.getState()==0)
						{	
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
						//		System.out.println(" Moving car from traffic light update , Time is :"+ currenttime);
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
								if(light2.getFlag_Coordinated()==true)
								{
									//Creating a traffic light update event for next light of cars path.
									eventqueue.add(new LightEvent(c.getCurrentLight(), currenttime+c.getTime()));
									light2.unsetFlag_Coordinated();
								}
							}
						}
						light2.decMLCarsToMove();
						}
					}
					
					if(light2.getRLCarsToMove()>0)
					{
						if(light2.getRightLaneSize()!=0)
						{
						Car c=light2.getRightLane().get(0);
						if(c.getState()==0)
						{	
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==Lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=Lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light update , Time is :"+ currenttime);
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
								if(light2.getFlag_Coordinated1()==true || (light2.s1==true && c.getCurrentLight().getTrafficDirection()!=light2.getTrafficDirection()))
								{
									//Creating a traffic light update event for next light of cars path.
									//if(c.getCurrentLight().getTrafficDirection()!=light2.getTrafficDirection())
									if(c.getCurrentLight().getTrafficDirection()==light2.getTrafficDirection())
										light2.s1=true;
									eventqueue.add(new LightEvent(c.getCurrentLight(), currenttime+c.getTime()));
									light2.unsetFlag_Coordinated1();
									if(light2.s1==true&&c.getCurrentLight().getTrafficDirection()!=light2.getTrafficDirection())
										light2.s1=false;
								}
							}
						}
						light2.decRLCarsToMove();
						}
					}
					
					//We need to move cars from front of each lane.
					
					if(light2.getRemainingTime()==0) // Change light to yellow.
					{
						// Set light to yellow and move the front car of each lane to next traffic light.
						light2.setlighttoyellow();
						light2.s1=false;
						light2.s2=false;
					//	light2.unsetFlag_Coordinated1();
					}
				}
				else if(light2.getCurrentLightColor()==LightColor.yellow)
				{
					light2.decRemainingtime();
					if(light2.getRemainingTime()==light2.getYellowTime()-1)
					{
						ArrayList<Car> leftlane=light2.getLeftLane();
						ArrayList<Car> middlelane=light2.getMiddleLane();
						ArrayList<Car> rightlane=light2.getRightLane();
						if(leftlane.size()!=0)
						{
						Car c1=light2.getLeftLane().get(0);
						if(c1.getState()==0)
						{
							if( c1.getNextLightIndex()==c1.path.size() ||(c1.getCurrentLane()==Lane.middle && c1.path.get(c1.getNextLightIndex()).getMiddleLaneSize()<=c1.getCurrentLight().getLaneLimit()) || ( c1.getCurrentLane()!=Lane.middle &&(c1.path.get(c1.getNextLightIndex()).getNumberOfTurningCars()<=c1.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light yellow update , Time is :"+ currenttime);
								int check=c1.getCurrentLight().removecar(c1, currenttime);
								c1.moving();
								if(check==1)
									eventqueue.add(c1.generateCarUpdateEvent(currenttime));
							}
						}
						}
						if(middlelane.size()!=0)
						{
						Car c2=middlelane.get(0);
						if(c2.getState()==0)
						{
							if( c2.getNextLightIndex()==c2.path.size() ||(c2.getCurrentLane()==Lane.middle && c2.path.get(c2.getNextLightIndex()).getMiddleLaneSize()<=c2.getCurrentLight().getLaneLimit()) || ( c2.getCurrentLane()!=Lane.middle &&(c2.path.get(c2.getNextLightIndex()).getNumberOfTurningCars()<=c2.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light yellow update , Time is :"+ currenttime);
								int check=c2.getCurrentLight().removecar(c2, currenttime);
								c2.moving();
								if(check==1)
									eventqueue.add(c2.generateCarUpdateEvent(currenttime));
							}
						}
						}
						if(rightlane.size()!=0)
						{
						Car c3=rightlane.get(0);
						if(c3.getState()==0)
						{
							if( c3.getNextLightIndex()==c3.path.size() ||(c3.getCurrentLane()==Lane.middle && c3.path.get(c3.getNextLightIndex()).getMiddleLaneSize()<=c3.getCurrentLight().getLaneLimit()) || ( c3.getCurrentLane()!=Lane.middle &&(c3.path.get(c3.getNextLightIndex()).getNumberOfTurningCars()<=c3.getCurrentLight().getLaneLimit())))
							{
							//	System.out.println(" Moving car from traffic light yellow update , Time is :"+ currenttime);
								int check=c3.getCurrentLight().removecar(c3, currenttime);
								c3.moving();
								if(check==1)
									eventqueue.add(c3.generateCarUpdateEvent(currenttime));
							}
						
						}
						}
					}
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
							light2.setLLCarsToMove(numcars);
							if(numcars>=light2.getThreshold_Coordinated())
							{
								light2.setFlag_Coordinated1();
							}
							/*
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
							*/
						}
						else
						{
							light2.setLLCarsToMove(num1);
							if(num1>=light2.getThreshold_Coordinated())
							{
								light2.setFlag_Coordinated1();
							}
							/*
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
							*/
						}
						
						if(numcars<num2)
						{
							light2.setMLCarsToMove(numcars);
							if(numcars>=light2.getThreshold_Coordinated())
							{
								light2.setFlag_Coordinated();
							}
							/*
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
							*/
						}
						else
						{
							light2.setMLCarsToMove(num2);
							if(num2>=light2.getThreshold_Coordinated())
							{
								light2.setFlag_Coordinated();
							}
							/*
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
							*/
						}
						
						if(numcars<num3)
						{
							light2.setRLCarsToMove(numcars);
							if(numcars>=light2.getThreshold_Coordinated())
							{
								light2.setFlag_Coordinated1();
							}
							/*
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
							*/
						}
						else
						{
							light2.setRLCarsToMove(num3);
							if(num3>=light2.getThreshold_Coordinated())
							{
								light2.setFlag_Coordinated1();
							}
							/*
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
							*/
						}
					}
				}
			}
	}
}
