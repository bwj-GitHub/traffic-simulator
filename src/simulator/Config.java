package simulator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {
	public long randomSeed;
	public int numrows=50;
	public int numcol=50;
	public float lambda;
	public int timelimit=5000;
	public int distrows=100;
	public int distcols=100;
	public int carspeed=10;
	public int carsize=5;
	public int caracceleration;
	public int carspacing;
	public int numcars;
	public int redtime;
	public int greentime;
	public int yellowtime;
	public boolean debug;
	public int threshold_selfmanaged;
	public int threshold_coordinated;
	public int threshold_convoy;
	public int algorithm;
	/*
	public int nRows;  // n in project description
	public int nCols;  // m in project description
	float timeLimit;  // TODO: Read timelimit?

	float lambda;  // parameter for car arrival
	public float acceleration;
	float maxVelocity;

	public int[] dRows;  // Distance between rows i and i+1 (in units c)
	public int[] dCols;  // Distance between cols i and i+1

	int[] nAvenueLanes;  // number of lanes in each avenue
	int[] nStreetLanes;  // number of lanes in each street
	*/
	
	public Config(int rows,int columns,int time,float lambda,int rowgap,int colgap,int speed,int acceleration,int size,int spacing,int numcars,int redtime,int greentime,int yellowtime,boolean debug,int threshold_selfmanaged,int threshold_coordinated,int threshold_convoy,int algorithm)
	{
		this.numrows=rows;
		this.numcol=columns;
		this.timelimit=time;
		this.lambda=lambda;
		this.distrows=rowgap;
		this.distcols=colgap;
		this.carspeed=speed;
		this.caracceleration=acceleration;
		this.carsize=size;
		this.carspacing=spacing;
		this.numcars=numcars;
		this.redtime=redtime;
		this.greentime=greentime;
		this.yellowtime=yellowtime;
		this.debug=debug;
		this.threshold_selfmanaged=threshold_selfmanaged;
		this.threshold_coordinated=threshold_coordinated;
		this.threshold_convoy=threshold_convoy;
		this.algorithm=algorithm;
	}
	
	public static Config readConfigFile(String filename)throws FileNotFoundException
	{
		Scanner sc;
		sc = new Scanner(new File(filename));
		int rows= sc.nextInt();
		int columns= sc.nextInt();
		int time= sc.nextInt();
		float lambda= sc.nextFloat();
		int rowgap= sc.nextInt();
		int colgap= sc.nextInt();
		int speed= sc.nextInt();
		int acceleration= sc.nextInt();
		int size= sc.nextInt();
		int spacing= sc.nextInt();
		int numcars=sc.nextInt();
		int redtime=sc.nextInt();
		int greentime=sc.nextInt();
		int yellowtime=sc.nextInt();
		int debug_value=sc.nextInt();
		int threshold_selfmanaged=sc.nextInt();
		int threshold_coordinated=sc.nextInt();
		int threshold_convoy=sc.nextInt();
		int algorithm=sc.nextInt();
		boolean debug;
		if(debug_value==1) debug=true;
		else debug=false;
		sc.close();
		return new Config(rows, columns, time, lambda, rowgap, colgap,speed, acceleration, size, spacing,numcars,redtime,greentime,yellowtime,debug,threshold_selfmanaged,threshold_coordinated,threshold_convoy,algorithm);
	}
}
