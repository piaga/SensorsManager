package model.arduino.sensors;
import java.util.UUID;
import model.sensors.AbstractSensor;
import model.sensors.enumerators.SENSOR_TYPE;

public abstract class ArduinoSensor<T> extends AbstractSensor<T> {

	private ArduinoSensor(UUID id, SENSOR_TYPE sensorType) {
		super(id, sensorType);
		// TODO Auto-generated constructor stub
	}

}
