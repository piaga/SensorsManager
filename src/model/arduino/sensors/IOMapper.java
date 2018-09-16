package model.arduino.sensors;

import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_TYPE;
import scala.NotImplementedError;

public class IOMapper {
	
	public static EntrySet MapSignal(String signal) throws Exception
	{
		
		Map map=new Gson().fromJson(signal, Map.class);
		Optional<SENSOR_TYPE> type=SENSOR_TYPE.GetById((int) map.get("ST"));
		
		if (!type.isPresent())
			throw new Exception("Invalid sensor data");
		
		
		throw new NotImplementedError();
		
	}
	
	
	
	
	
	

}
