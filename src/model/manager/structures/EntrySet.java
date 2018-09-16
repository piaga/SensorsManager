package model.manager.structures;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import model.sensors.enumerators.*;


public class EntrySet {
	
	private String generic; 
	private SENSOR_TYPE sensor;
	private Object value;
	private String timestamp;

	public EntrySet(SENSOR_TYPE type,Object object)
	{
		this.sensor=type;
		this.value=object;
		this.generic=this.value.getClass().getSimpleName();
		//this.timestamp=LocalDateTime.now().toString();
		this.timestamp=System.currentTimeMillis()+"";
	}
	
	public SENSOR_TYPE getType() {return this.sensor;}
	
	public Object getObject() {return this.value;}
	
	
}
