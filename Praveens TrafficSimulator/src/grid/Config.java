package grid;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {

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
	/*
	public int nRows;  // n in project description
	public int nCols;  // m in project description
	float timeLimit;  // TODO: Read timelimit?
	long randomSeed; // TODO: Read this?

	float lambda;  // parameter for car arrival
	public float acceleration;
	float maxVelocity;

	public int[] dRows;  // Distance between rows i and i+1 (in units c)
	public int[] dCols;  // Distance between cols i and i+1

	int[] nAvenueLanes;  // number of lanes in each avenue
	int[] nStreetLanes;  // number of lanes in each street
	*/
	
	public Config(int rows,int columns,int time,float lambda,int rowgap,int colgap,int speed,int acceleration,int size,int spacing,int numcars,int redtime,int greentime,int yellowtime)
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
		sc.close();
		return new Config(rows, columns, time, lambda, rowgap, colgap,speed, acceleration, size, spacing,numcars,redtime,greentime,yellowtime);
	}
}