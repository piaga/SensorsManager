package model.manager.tests;

import java.util.ArrayList;
import java.util.Optional;

import model.manager.ISensorsManager;
import model.manager.SensorsManager;
import model.sensors.*;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.Vector3;
import model.sensors.virtual.WorkingRealVirtualSensor;

public class SensorsObserverTest {
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Ready...");
		ISensorsManager manager=SensorsManager.getSensorsObserver(0, Optional.empty());
		ISensor accelerometer=new WorkingRealVirtualSensor<Vector3>(SensorsManager.getNewId(), new Vector3(), -20, 20, 2, 10, SENSOR_TYPE.ACCELEROMETER, SENSOR_SIMULATION_PATTERN.RANDOM, Optional.empty());
		ISensor luminosity = new WorkingRealVirtualSensor<Double>(SensorsManager.getNewId(), (double) 400, 0, 600, 5, 10, SENSOR_TYPE.LIGHT, SENSOR_SIMULATION_PATTERN.QUADRATIC, Optional.ofNullable(1));
		manager.addSensor(accelerometer);
		manager.addSensor(luminosity);
		Thread.sleep(2000);
		manager.removeSensor(accelerometer);
		Thread.sleep(2000);
		manager.removeSensor(luminosity);
		
		
	}
}
