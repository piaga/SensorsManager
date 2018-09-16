package model.manager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;



import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.structures.DataSet;
import model.manager.structures.EntrySet;
import model.sensors.AbstractSensor;
import model.sensors.ISensor;
import model.sensors.enumerators.SENSOR_TYPE;

@Component("SensorsManager")
public class SensorsManager extends AbstractSensorManager implements ISensorsManager{
	
	
	private static Optional<SensorsManager> sensorsObserver=Optional.empty();
	
	private HashMap<SENSOR_TYPE,Optional<ISensor>> sensorsMap;
	private ArrayList<Observer<EntrySet>> observers;

	private SensorsManager(long id) {
		super(id);
		this.dataSet=DataSet.getDataSet();
		sensorsMap=new HashMap<SENSOR_TYPE,Optional<ISensor>>();
		observers=new ArrayList<Observer<EntrySet>>();
	}
	
	private SensorsManager(long id, ArrayList<SENSOR_TYPE> sensorTypes)	
	{
		super(id);
		this.dataSet=DataSet.getDataSet(sensorTypes);
		sensorsMap=new HashMap<SENSOR_TYPE,Optional<ISensor>>();
		
	}
	
	public static SensorsManager getSensorsObserver(long id,Optional<ArrayList<SENSOR_TYPE>> sensorTypes)
	{
		SensorsManager manager;
		if (sensorsObserver.isPresent())
			manager= sensorsObserver.get();
		
		if (sensorTypes.isPresent())
			manager= new SensorsManager(id, sensorTypes.get());
		
		manager= new SensorsManager(id);
		System.out.println("[SensorsManager] Getting instance of id: "+manager.getId());
		return manager;
	}
	

	

	@Override
	public void addSensor(ISensor sensor) throws Exception{
		if (!sensorsMap.containsKey(sensor.getSensorType()) || 
				!sensorsMap.get(sensor.getSensorType()).isPresent())
		{
			sensorsMap.put(sensor.getSensorType(), Optional.ofNullable(sensor));
			this.dataSet.addSensorType(sensor.getSensorType());
			sensor.getObservable().subscribe(this);
		}
		else log(String.format("A sensor of type %s is already in."
				+ " Please remove it before add another one!", sensor.getSensorType().toString()));
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeSensor(ISensor sensor) {
		
		if (sensorsMap.containsKey(sensor.getSensorType())&&sensorsMap.get(sensor.getSensorType()).isPresent())
		{
			ISensor removed=sensorsMap.remove(sensor.getSensorType()).get();
			((AbstractSensor<Object>)removed).disconnectObserver();
			this.dataSet.removeSensorType(removed.getSensorType());
		}
		else log(String.format("The sensor you trying to remove does not exists!", sensor.getSensorType()));
	}

	@Override
	public Optional<ISensor> getSensorByUUID(UUID id) {
		
		for(Optional<ISensor> sensor:sensorsMap.values())
			if (sensor.isPresent()&& id.compareTo(sensor.get().getGuid())==0)
				return sensor;
		return Optional.empty();
	}

	@Override
	public Optional<ISensor> getSensorByType(SENSOR_TYPE type) {
		for(Entry<SENSOR_TYPE,Optional<ISensor>> sensor:sensorsMap.entrySet())
			if (sensor.getKey()==type && sensor.getValue().isPresent())
				return sensor.getValue();
		return Optional.empty();
	}

	
	//Method from observer interface
	@Override
	public void onComplete() {
		if (sensorsMap.size()==0)
			log("No sensors to watch");

	}

	@Override
	public void onError(Throwable arg0) {
		System.err.println(arg0.getMessage());

	}

	@Override
	public synchronized void onNext(EntrySet entry) {
		//System.out.println(new Gson().toJson(entry));
		dataSet.updateData(entry.getType(), entry.getObject());
		//System.out.println(dataSet.toString());
		observers.forEach(observer->observer.onNext(entry));}

	@Override
	public void onSubscribe(Disposable disposable) {
		ISensor sensor=(ISensor)disposable;
		System.out.println("[SensorsObserver] Connected to sensor "+sensor.getGuid());

	}

	//METHOD FROM Observable
	@SuppressWarnings("unchecked")
	@Override
	protected void subscribeActual(Observer<? super EntrySet> observer) {
		this.observers.add((Observer<EntrySet>) observer);
		this.sensorsMap.values().forEach(f->{if (f.isPresent()) f.get().start();});
		
		
	}
	
	public final void disconnectObserver(Observer<? super EntrySet> observer)
	{
		log("Disconnecting observer...");
		try
		{
			if (observers.contains(observer))
			{
				
				observers.remove(observer);
				observer.onComplete();
				if (observers.size()==0)
					this.sensorsMap.values().forEach(f->{if(f.isPresent())f.get().stop();});
				
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

	@Override
	public ArrayList<ISensor> getSensorsList() throws Exception {
		
		ArrayList<ISensor> list=new ArrayList<ISensor>();
		this.sensorsMap.values().parallelStream().forEach(element->list.add(element.get()));
		return list;
	}
	
	public ArrayList<EntrySet> getSensorsDetails()
	{
		ArrayList<EntrySet> sensorsDetails=
				(ArrayList<EntrySet>)sensorsMap.entrySet()
										.parallelStream()
										.filter(p->p.getValue().isPresent()&&p.getValue().get().getSensorDetails().size()>0)
										.map(m->new EntrySet(m.getKey(),m.getValue().get().getSensorDetails()))
										.collect(Collectors.toList());
																													
																													
		return sensorsDetails;
	}

}
