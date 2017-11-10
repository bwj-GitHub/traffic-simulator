package simulator;

import java.util.Random;

public class InterarrivalTimeGenerator {

	double lambda;  // rate parameter (average cars per unit time)
	Random random;

	public InterarrivalTimeGenerator(double lambda, Random random) {
		this.lambda = lambda;
		this.random = random;
	}

	public double getNextArrivalTime() {
		// Code found at: https://stackoverflow.com/questions/29020652/java-exponential-distribution
		//  answer by Florian Prud'homme
	    return  Math.log(1-random.nextDouble())/(-lambda);
	    // log(1 - u) / lambda
	}

//	public static void main(String args[]) {
//		Random rand = new Random();
//		InterarrivalTimeGenerator I1 = new InterarrivalTimeGenerator(10, rand);
//		InterarrivalTimeGenerator I2 = new InterarrivalTimeGenerator(5, rand);
//		
//		System.out.println("I1:");
//		System.out.println(I1.getNextArrivalTime());
//		System.out.println(I1.getNextArrivalTime());
//		System.out.println(I1.getNextArrivalTime());
//		
//		System.out.println("I2:");
//		// Higher Lambda means that interarrival times get smaller with time
//		System.out.println(I2.getNextArrivalTime());
//		System.out.println(I2.getNextArrivalTime());
//		System.out.println(I2.getNextArrivalTime());
//
//	}

}
