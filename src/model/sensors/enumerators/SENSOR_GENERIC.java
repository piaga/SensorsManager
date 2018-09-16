package model.sensors.enumerators;

import java.util.Arrays;
import java.util.Optional;

public enum SENSOR_GENERIC {
	BOOLEAN(1),
	SCALAR(2),
	VECTOR3(3),
	IMAGE(4),
	COORDINATES(5);
	
	private final int id;
	
	private SENSOR_GENERIC(int id) {
		this.id=id;
	}
	
	public Optional<SENSOR_GENERIC> getSensorGenericById(int id)
	{
		return Arrays.stream(values()).filter(f->f.id==id).findFirst();
	}
}
