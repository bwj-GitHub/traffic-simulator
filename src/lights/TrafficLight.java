package lights;

import traffic.TrafficQueue;

public class TrafficLight {
	public boolean isGreen;
	public boolean isRed;
	public float nextChangeTime;
	public TrafficQueue trafficQueue;
	
	public boolean isGreen() {
		return isGreen;
	}
}
