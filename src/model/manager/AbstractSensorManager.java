package model.manager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.structures.DataSet;
import model.manager.structures.EntrySet;
import model.sensors.ISensor;
import java.util.UUID;

public abstract class AbstractSensorManager extends Observable<EntrySet> implements Observer<EntrySet> {

	
	private static long identificatorFactory=0;
	
	protected long id;
	protected DataSet dataSet;
	
	public AbstractSensorManager(long id)
	{
		this.id=id;
	}

	public void log(String message)
	{
		System.out.println(String.format("[O%d] %s", id,message));
	}
	
	protected final Observer<EntrySet> getObserver()
	{
		return this;
	}
	
	public long getId() {
		return this.id;
	}
	
	public static UUID getNewId()
	{
		identificatorFactory++;
		return UUID.fromString(identificatorFactory+""); 
	}
	
	public abstract void addSensor(ISensor sensor) throws Exception;
	public abstract void removeSensor(ISensor sensor) throws Exception;
	
}
