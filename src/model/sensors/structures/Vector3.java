package model.sensors.structures;

import java.util.Random;

public class Vector3 {
	
	public Double x,y,z;
	
	public Vector3()
	{
		this.x=new Double(0);
		this.y=new Double(0);
		this.z=new Double(0);
	}
	
	public Vector3(Double x,Double y,Double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("[%f,%f,%f]", this.x,this.y,this.z);
	}
	
	public static double rebound(double value, double minBound, double maxBound)
	{
		double x=value;
		
		if (x>maxBound)
			x=maxBound;
		else if (x<minBound)
			x=minBound;
		
		return x;
	}
	
	public static Vector3 incrementVector(Vector3 vector,Vector3 increment,double minBound, double maxBound)
	{
		Double x,y,z;
		//x component
		x=Vector3.rebound(vector.x+increment.x, minBound, maxBound); 
		
		//y component
		y=Vector3.rebound(vector.y+increment.y,minBound,maxBound);
		
		z=Vector3.rebound(vector.z+increment.z, minBound, maxBound);
		
		return new Vector3(x,y,z);
		
		
	}

	public static Vector3 random(Double maxPeak) {
		Random random = new Random();
		return new Vector3(
					new Double(random.nextDouble()*maxPeak*2-maxPeak),
					new Double(random.nextDouble()*maxPeak*2-maxPeak),
					new Double(random.nextDouble()*maxPeak*2-maxPeak));
	}
	

}
