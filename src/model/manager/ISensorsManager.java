package model.manager;
import model.sensors.*;
import model.sensors.enumerators.SENSOR_TYPE;

import java.util.ArrayList;
import java.util.Optional;

public interface ISensorsManager {
	
	public void addSensor(ISensor sensor) throws Exception;
	public void removeSensor(ISensor sensor) throws Exception;
	public ArrayList<ISensor> getSensorsList() throws Exception;
	public Optional<ISensor> getSensorById(long id) throws Exception;
	public Optional<ISensor> getSensorByType(SENSOR_TYPE type) throws Exception;

}
