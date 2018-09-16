package model.sensors.virtual;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.Vector3;

public class WorkingRealVirtualSensor<T extends Object> extends AbstractRealVirtualSensor<T> {
	
	private String type;
	
	
	
	
	
	

	public WorkingRealVirtualSensor(UUID id,T startValue,double minValue, double maxValue, double maxPeakFromLast, int timeForCapture,
			SENSOR_TYPE sensorType, SENSOR_SIMULATION_PATTERN pattern, Optional<Integer> patter_duration) throws Exception {
		
		super(id,startValue,minValue, maxValue, maxPeakFromLast, timeForCapture, sensorType, pattern, patter_duration);
		// TODO Auto-generated constructor stub
		
		
		if (pattern!=SENSOR_SIMULATION_PATTERN.RANDOM)
		{
			maxQuadratic=new Double(this.maxValue-this.maxPeakFromLast*1.2);
			minQuadratic=new Double(this.minValue+this.maxPeakFromLast*0.8);
			pulse= 2*Math.PI/this.pattern_duration.get();
			calculatePhase();
			
		}
		
				
		log(String.format("Created with parameters\n min:%f\n max:%f\n peak:%f\n type:%s\n pattern:%s", minValue,maxValue,maxPeakFromLast,sensorType.toString(),pattern.toString()));
	}
	
	
	
	


	@Override
	public void run() {
		this.time_start=System.currentTimeMillis();
		this.t1=this.time_start;
		this.t2=this.time_start;
		this.log("Running");
		try
		{
			while(this.status==SENSOR_STATUS.RUNNING)
			{
				switch(pattern)
				{
				case RANDOM:
					updateRandom();
					break;
				case QUADRATIC:
					updateQuadratic();
					break;
				case SINUSOIDAL:
					updateSinusoidal();
					break;
				default:
					throw new Exception("Simulation pattern not choosed");
				}
				
				if(!updateObserver(new EntrySet(this.getSensorType(),this.lastValue.get())))
					break;
				
					
				t2=System.currentTimeMillis();
				Thread.sleep(timeForCapture);
			}
			log("Running complete!");
			setStatus(SENSOR_STATUS.READY);
		}
		catch(Exception e)
		{
			log("Error while running: "+e.getMessage());
			e.printStackTrace();
			setStatus(SENSOR_STATUS.DAMAGED);
		}
		
	}
	
	//SINUSOIDAL & QUADRATIC UPDATES


	

	
	
	

}
