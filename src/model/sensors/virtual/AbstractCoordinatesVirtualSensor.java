package model.sensors;

import java.util.Optional;

import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.GeographicCoordinates;

public abstract class AbstractCoordinatesVirtualSensor extends AbstractSensor<GeographicCoordinates> implements ISensor{

	protected double maxPeakFromLast;
	protected int timeForCapture;
	protected Optional<GeographicCoordinates> lastValue;

	public AbstractCoordinatesVirtualSensor(long id, SENSOR_TYPE sensorType) {
		super(id, sensorType);
		this.details.addDetail(SENSOR_DETAIL.DELAY, this.timeForCapture);
	}
	
	public AbstractCoordinatesVirtualSensor(long id,Optional<GeographicCoordinates> startValue, double maxPeakFromLast,int timeForCapture,SENSOR_TYPE sensorType) {
		super(id, sensorType);
		this.maxPeakFromLast=maxPeakFromLast;
		this.timeForCapture=timeForCapture;
		
		if (startValue.isPresent())
			lastValue=startValue;
		else
			lastValue=Optional.ofNullable(new GeographicCoordinates());
			
			
		
		this.status=SENSOR_STATUS.READY;
	}
	
	@Override
	public abstract void run();
	
	@Override
	public final String toString()
	{
		return String.format("[%s] DELAY: %d", this.timeForCapture);
	}
	



	

}
