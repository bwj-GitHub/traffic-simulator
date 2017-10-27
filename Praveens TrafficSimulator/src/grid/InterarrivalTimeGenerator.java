package grid;
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
}
