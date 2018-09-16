package model.sensors.virtual;

import java.util.UUID;
import java.util.Optional;
import java.util.Random;

import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.GeographicCoordinates;

public class WorkingCoordinatesVirtualSensor extends AbstractCoordinatesVirtualSensor {
	
	private long time_start,t1,t2;
	
	
	private Random random;
	

	public WorkingCoordinatesVirtualSensor(UUID id,Optional<GeographicCoordinates> startValue, double randomMetersFromLast, int timeForCapture,
			SENSOR_TYPE sensorType) {
		super(id,startValue,randomMetersFromLast, timeForCapture, sensorType);
		random=new Random();
		
		
		log(String.format("Created with parameters\n latitude: %f\n longitude: %f\n delay:%d",
				this.lastValue.get().getLatitude(),
				this.lastValue.get().getLongitude(),
				this.timeForCapture));
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
				
				double incrementalLatitude=(random.nextDouble()*this.maxPeakFromLast*2-this.maxPeakFromLast)*GeographicCoordinates.meterToDegrees;
				double incrementalLongitude=(random.nextDouble()*this.maxPeakFromLast*2-this.maxPeakFromLast)*GeographicCoordinates.meterToDegrees;
				this.lastValue=Optional.ofNullable(GeographicCoordinates.addToCoordinates(this.lastValue.get(), incrementalLatitude,incrementalLongitude));
				if (!updateObserver(new EntrySet(getSensorType(), this.lastValue)))
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

}
