package model.sensors;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.Observer;
import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.SensorDetails;

public abstract class AbstractSensor<T extends Object> extends Observable<EntrySet> implements ISensor {
	
	Thread thread;
	protected UUID id;
	protected final SENSOR_TYPE sensorType;
	protected SENSOR_STATUS status;
	protected SensorDetails details;
	protected Optional<Observer<? super EntrySet>> connectedObserver;
	
	
	public AbstractSensor(UUID id, SENSOR_TYPE sensorType) {
		this.id=id;
		this.connectedObserver=Optional.empty();
		this.sensorType=sensorType;
		this.details=new SensorDetails();
	}
	
	@Override
	public final HashMap<SENSOR_DETAIL, Object> getSensorDetails()
	{
		return this.details.getDetails();
	}
	
	@Override
	public abstract void run();
	
	protected boolean updateObserver(EntrySet entry)
	{
		if (!this.connectedObserver.isPresent())
			return false;
		this.connectedObserver.get().onNext(entry);
		return true;
	}
	
	protected void setStatus(SENSOR_STATUS status)
	{
		updateObserver(new EntrySet(sensorType, status));
		this.status=status;
		
	}
	
	@Override
	public SENSOR_STATUS getStatus()
	{
		return this.status;
	}

	@Override
	public final SENSOR_TYPE getSensorType() {
		return sensorType;
	}
	
	
	
	@Override
	public final UUID getGuid()
	{
		return this.id;
	}
	
	
	@Override
	protected final void subscribeActual(Observer<? super EntrySet> observer)
	{
		log("New observer is connecting...");
		try
		{	
			if (!this.connectedObserver.isPresent())
				{
				this.connectedObserver=Optional.ofNullable(observer);
				
			}
		}
		catch(Exception e)
		{
			log("Impossible to connect the observer");
		}
		finally {
			log("Observer connected");
		}
		
		
	}
	
	public final void disconnectObserver()
	{
		log("Disconnecting observer...");
		try
		{
			if (connectedObserver.isPresent())
			{
				connectedObserver.get().onComplete();
				connectedObserver=Optional.empty();
				
				
				
			}
			else
				throw new Exception("Observer not found!");
			
		}
		catch(Exception e)
		{
			log("Error while disconnecting..."+e.getMessage());
		}
		finally {
			log("Observer disconnected");
		}
	}
	
	public final void log(String message)
	{
		System.out.println(String.format("[%s] %s", getSensorType().toString(),message));
	}
	
	@Override
	public Observable<EntrySet> getObservable() {
		return this;
	
	}

	@Override
	public void start() {
		log("Starting...");
		this.thread=new Thread(this);
		try
		{
			if (this.status==SENSOR_STATUS.READY)
			{
				
				setStatus(SENSOR_STATUS.RUNNING);
				thread.start();
				log("Started!");
			}
			else
			{
				log("This sensor is not ready");
			}
		}
		catch(Exception e)
		{
			setStatus(SENSOR_STATUS.DAMAGED);
			log("Unable to start for: "+e.getMessage());
		}
		
	}

	@Override
	public void stop()
	{
		log("Stopping...");
		try
		{
				if (getStatus()==SENSOR_STATUS.RUNNING)
					setStatus(SENSOR_STATUS.STOP);
				else
					setStatus(SENSOR_STATUS.READY);
		}
		catch(Exception e)
		{
			log("Unable to stop for: "+e.getMessage());
		}
	}
	
	

}


