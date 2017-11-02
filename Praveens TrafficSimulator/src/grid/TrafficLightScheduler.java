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
				TrafficLight light1=grid.getIntersection(i, j).getlight1();
				TrafficLight light2=grid.getIntersection(i, j).getlight2();
				light1.unsetFlag();
				light2.unsetFlag();
				if(light1.getCurrentLightColor()==lightcolor.green)
				{
					light1.decRemainingtime();
					if(light1.getLLCarsToMove()>0)
					{
						if(light1.getLeftLaneSize()!=0)
						{
						Car c=light1.getLeftLane().get(0);
						if(c.getState()==0)
						{
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
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
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
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
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
							}
						}
						light1.decRLCarsToMove();
						}
					}
					if(light1.getRemainingTime()==0) // Change light to yellow.
					{
						light1.setlighttoyellow();
					}
				}
				else if(light1.getCurrentLightColor()==lightcolor.yellow)
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
							if( c1.getNextLightIndex()==c1.path.size() ||(c1.getCurrentLane()==lane.middle && c1.path.get(c1.getNextLightIndex()).getMiddleLaneSize()<=c1.getCurrentLight().getLaneLimit()) || ( c1.getCurrentLane()!=lane.middle &&(c1.path.get(c1.getNextLightIndex()).getNumberOfTurningCars()<=c1.getCurrentLight().getLaneLimit())))
							{
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
							if( c2.getNextLightIndex()==c2.path.size() ||(c2.getCurrentLane()==lane.middle && c2.path.get(c2.getNextLightIndex()).getMiddleLaneSize()<=c2.getCurrentLight().getLaneLimit()) || ( c2.getCurrentLane()!=lane.middle &&(c2.path.get(c2.getNextLightIndex()).getNumberOfTurningCars()<=c2.getCurrentLight().getLaneLimit())))
							{
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
							if( c3.getNextLightIndex()==c3.path.size() ||(c3.getCurrentLane()==lane.middle && c3.path.get(c3.getNextLightIndex()).getMiddleLaneSize()<=c3.getCurrentLight().getLaneLimit()) || ( c3.getCurrentLane()!=lane.middle &&(c3.path.get(c3.getNextLightIndex()).getNumberOfTurningCars()<=c3.getCurrentLight().getLaneLimit())))
							{
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
							light1.setLLCarsToMove(numcars);
						}
						else
						{
							light1.setLLCarsToMove(num1);
						}
						
						if(numcars<num2)
						{
							light1.setMLCarsToMove(numcars);
						}
						else
						{
							light1.setMLCarsToMove(num2);
						}
						
						if(numcars<num3)
						{
							light1.setRLCarsToMove(numcars);
						}
						else
						{
							light1.setRLCarsToMove(num3);
						}
					}
				}
				
				if(light2.getCurrentLightColor()==lightcolor.green)
				{
					light2.decRemainingtime();
					
					if(light2.getLLCarsToMove()>0)
					{
						if(light2.getLeftLaneSize()!=0)
						{
						Car c=light2.getLeftLane().get(0);
						if(c.getState()==0)
						{	
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
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
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
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
							if( c.getNextLightIndex()==c.path.size() ||(c.getCurrentLane()==lane.middle && c.path.get(c.getNextLightIndex()).getMiddleLaneSize()<=c.getCurrentLight().getLaneLimit()) || ( c.getCurrentLane()!=lane.middle &&(c.path.get(c.getNextLightIndex()).getNumberOfTurningCars()<=c.getCurrentLight().getLaneLimit())))
							{
								int check=c.getCurrentLight().removecar(c, currenttime);
								c.moving();
								if(check==1)
									eventqueue.add(c.generateCarUpdateEvent(currenttime));
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
						
					}
				}
				else if(light2.getCurrentLightColor()==lightcolor.yellow)
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
							if( c1.getNextLightIndex()==c1.path.size() ||(c1.getCurrentLane()==lane.middle && c1.path.get(c1.getNextLightIndex()).getMiddleLaneSize()<=c1.getCurrentLight().getLaneLimit()) || ( c1.getCurrentLane()!=lane.middle &&(c1.path.get(c1.getNextLightIndex()).getNumberOfTurningCars()<=c1.getCurrentLight().getLaneLimit())))
							{
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
							if( c2.getNextLightIndex()==c2.path.size() ||(c2.getCurrentLane()==lane.middle && c2.path.get(c2.getNextLightIndex()).getMiddleLaneSize()<=c2.getCurrentLight().getLaneLimit()) || ( c2.getCurrentLane()!=lane.middle &&(c2.path.get(c2.getNextLightIndex()).getNumberOfTurningCars()<=c2.getCurrentLight().getLaneLimit())))
							{
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
							if( c3.getNextLightIndex()==c3.path.size() ||(c3.getCurrentLane()==lane.middle && c3.path.get(c3.getNextLightIndex()).getMiddleLaneSize()<=c3.getCurrentLight().getLaneLimit()) || ( c3.getCurrentLane()!=lane.middle &&(c3.path.get(c3.getNextLightIndex()).getNumberOfTurningCars()<=c3.getCurrentLight().getLaneLimit())))
							{
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
						}
						else
						{
							light1.setLLCarsToMove(num1);
						}
						
						if(numcars<num2)
						{
							light1.setMLCarsToMove(numcars);
						}
						else
						{
							light1.setMLCarsToMove(num2);
						}
						
						if(numcars<num3)
						{
							light1.setRLCarsToMove(numcars);
						}
						else
						{
							light1.setRLCarsToMove(num3);
						}
					}
				}
			}
	}
}
