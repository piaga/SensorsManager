package model.manager.structures;
import model.sensors.enumerators.*;
import model.sensors.structures.Vector3;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Optional;

import com.google.gson.Gson;

public class DataSet {
	
	private static Optional<DataSet> dataSet=Optional.empty();
	private HashMap<SENSOR_TYPE,Object> data=new HashMap<SENSOR_TYPE,Object>();
	
	private DataSet() {
		
	}
	
	private DataSet(ArrayList<SENSOR_TYPE> typeList) {
		
		for(SENSOR_TYPE type:typeList)
		{
			data.put(type, Optional.empty());
		}
	}
	
	public static DataSet getDataSet()
	{
		if (dataSet.isPresent())
			return dataSet.get();
		
		dataSet=Optional.ofNullable(new DataSet());
		return dataSet.get();
	}


	public static DataSet getDataSet(ArrayList<SENSOR_TYPE> typeList)
	{
		if (dataSet.isPresent())
			return dataSet.get();
		
		dataSet=Optional.ofNullable(new DataSet(typeList));
		return dataSet.get();
	}
	
	public void addSensorType(SENSOR_TYPE sensorType)
	{
		if (!data.containsKey(sensorType))
			data.put(sensorType, Optional.empty());
		else
			System.err.println("[DATASET] The sensor type is already in there!");
	}
	
	public void removeSensorType(SENSOR_TYPE sensorType)
	{
		if (data.containsKey(sensorType))
			data.remove(sensorType, data.get(sensorType));
		else
			System.err.println("[DATASET] The sensor type not exists");
	}
	
	public void updateData(SENSOR_TYPE type, Object value)
	{
		this.data.replace(type, value);
		
	}
	
	
	@Override
	public String toString() {
		StringBuilder stringBuilder=new StringBuilder();
		
		try
		{
			
			stringBuilder.append("{\r\n");
			for(Entry<SENSOR_TYPE,Object> set: data.entrySet())
			{
				SENSOR_TYPE key=set.getKey();
				Object value=(Object)set.getValue();
				if (value instanceof Double)
				{
					double doubleValue=((Double)value).doubleValue();
					stringBuilder.append(String.format("[%s:%f]\r\n", key.toString(),doubleValue));
				}
				else if (value instanceof Vector3)
				{
					Vector3 vector=(Vector3)value;
					stringBuilder.append(String.format("[%s:%f|%f|%f]\r\n", key.toString(),
							vector.x,
							vector.y,
							vector.z));
				}
				else throw new Exception("Type "+value.getClass().getSimpleName()+" not implemented yet");
			}
			
			
		}
		catch(Exception e)
		{
			stringBuilder.append("Type data not implemented yet! "+e.getMessage());
		}
		finally
		{
			stringBuilder.append("\r\n}");
		}
		return stringBuilder.toString();
	}
	
	
	
	
	
	
	
	
	

}
