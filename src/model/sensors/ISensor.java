package model.sensors;

import java.util.HashMap;
import java.util.*;
import io.reactivex.Observable;
import model.manager.structures.EntrySet;
import model.sensors.enumerators.*;

public interface ISensor extends Runnable{
	
	
	public SENSOR_STATUS getStatus();
	
	public SENSOR_TYPE getSensorType();
	
	public HashMap<SENSOR_DETAIL, Object> getSensorDetails();
	
	public UUID getGuid();
	
	public Observable<EntrySet> getObservable();
	
	
	public void start();
	
	public void stop();
	
	

}
