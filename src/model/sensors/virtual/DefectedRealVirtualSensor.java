package model.sensors.virtual;
import java.util.UUID;
import java.util.Optional;

import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;

public class DefectedRealVirtualSensor<T extends Object> extends AbstractRealVirtualSensor<T> {
	
	private int millisToDefect; 
	private double percentOfDefect;

	public DefectedRealVirtualSensor(UUID id, T startValue, double minValue, double maxValue,
			double maxPeakFromLast, int timeForCapture,int millisToDefect,double percentOfDefect, SENSOR_TYPE sensorType, SENSOR_SIMULATION_PATTERN pattern,
			Optional<Integer> pattern_duration) throws Exception {
		super(id, startValue, minValue, maxValue, maxPeakFromLast, timeForCapture, sensorType, pattern, pattern_duration);
		this.millisToDefect=millisToDefect;
		calculatePhase();
		if (percentOfDefect<0)
			this.percentOfDefect=0;
		else if(percentOfDefect>100)
			this.percentOfDefect=100;
		else
			this.percentOfDefect=percentOfDefect;
	}

	@Override
	public void run() {
		this.time_start=System.currentTimeMillis();
		this.t1=this.time_start;
		this.t2=this.time_start;
		this.log("Running");
		try
		{
			while(this.status==SENSOR_STATUS.RUNNING||this.status==SENSOR_STATUS.DAMAGED)
			{
				switch(this.status)
				{
				case RUNNING:
					switch(this.pattern)
					{
					case QUADRATIC:
						updateQuadratic();
						break;
					case RANDOM:
						updateRandom();
						break;
					case SINUSOIDAL:
						updateSinusoidal();
						break;
						default:break;
					}
					updateObserver(new EntrySet(sensorType, lastValue.get()));
					if (random.nextDouble()*100>(100-percentOfDefect))
						setStatus(SENSOR_STATUS.DAMAGED);
					t2=System.currentTimeMillis();
					Thread.sleep(timeForCapture);
					break;
				case DAMAGED:
					if (random.nextDouble()*100>percentOfDefect)
						setStatus(SENSOR_STATUS.RUNNING);
					t2=System.currentTimeMillis();
					Thread.sleep(millisToDefect);
					break;
				default:
					break;
				}
				
				
			}
		}
		catch(Exception e)
		{
			log("Error while running: "+e.getMessage());
			e.printStackTrace();
			setStatus(SENSOR_STATUS.DAMAGED);
		}
		

	}

}
