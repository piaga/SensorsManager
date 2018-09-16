package model.sensors.virtual;
import java.util.UUID;
import model.manager.structures.EntrySet;
import model.sensors.AbstractSensor;
import model.sensors.ISensor;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;

public class WorkingDependantRealVirtualSensor<T> extends AbstractSensor<T> {
	
	//SENSORE DUMMY CHE RESTITUISCE UN RISULTATO DIPENDENTE DA UN ALTRO SENSORE
	ISensor sensorParent;
	int count=0;
	double min=0,max=1000;
	
	public WorkingDependantRealVirtualSensor(UUID id, SENSOR_TYPE sensorType,ISensor sensor) throws Exception{
		super(id, sensorType);
		if (sensor instanceof WorkingImageVirtualSensor)
			this.sensorParent=sensor;
		else
			throw new Exception("WDRVS sensor parent not implemented yet");
		
		this.details.addDetail(SENSOR_DETAIL.DELAY, sensor.getSensorDetails().get(SENSOR_DETAIL.DELAY));
		this.details.addDetail(SENSOR_DETAIL.MIN_VALUE, min);
		this.details.addDetail(SENSOR_DETAIL.MAX_VALUE, max);
		
	}

	@Override
	public void run() {
		
		
		
	}

}
