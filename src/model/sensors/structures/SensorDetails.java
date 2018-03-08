package model.sensors.structures;

import model.sensors.enumerators.*;


import java.util.HashMap;
import java.util.Optional;

public class SensorDetails {
	HashMap<SENSOR_DETAIL, Object> details;
	
	public SensorDetails()
	{
		details=new HashMap<SENSOR_DETAIL,Object>();
	}
	
	public boolean addDetail(SENSOR_DETAIL key,Object value)
	{
		if (details.containsKey(key))
			return false;
		
		details.put(key, value);
		return true;
	}
	
	public Optional<Object> getDetail(SENSOR_DETAIL detail)
	{
		return Optional.ofNullable(details.get(detail));
	}
	
	public boolean removeDetail(SENSOR_DETAIL detail)
	{
		if (details.containsKey(detail))
			return false;
		details.remove(detail);
		return true;
	}

	public HashMap<SENSOR_DETAIL, Object> getDetails() {
		return details;
	}

	public void setDetails(HashMap<SENSOR_DETAIL, Object> details) {
		this.details = details;
	}
	
	
	

}
