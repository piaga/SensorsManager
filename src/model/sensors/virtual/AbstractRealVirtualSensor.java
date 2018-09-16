package model.sensors.virtual;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import model.manager.structures.EntrySet;
import model.sensors.AbstractSensor;
import model.sensors.ISensor;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_GENERIC;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.Vector3;

public abstract class AbstractRealVirtualSensor<T extends Object> extends AbstractSensor<T> implements ISensor {

	
	
	protected T startValue;
	protected Optional<T> lastValue;
	protected int timeForCapture;
	protected final double minValue,maxValue,maxPeakFromLast,range;
	
	protected final SENSOR_SIMULATION_PATTERN pattern;
	protected Optional<Integer> pattern_duration=Optional.empty(); //IN SECONDS
	
	protected SENSOR_GENERIC sensorGeneric;
	
	//UPDATE METHOD FIELDS
	protected Random random;
	protected long time_start,t1,t2;
	
	//NO-RANDOM UPDATE METHODS FIELDS
	protected Double maxQuadratic,minQuadratic;
	protected Object phase;
	protected double pulse;
	
	//QUADRATIC METHODS FIELDS
	protected double flatPositive, flatNegative, interval;
	
	public AbstractRealVirtualSensor(
			UUID id,
			T startValue,
			double minValue,
			double maxValue,
			double maxPeakFromLast,
			int timeForCapture,
			SENSOR_TYPE sensorType,
			SENSOR_SIMULATION_PATTERN pattern,
			Optional<Integer> pattern_duration) throws Exception
	{
		super(id,sensorType);
		
		this.minValue=minValue;
		this.maxValue=maxValue;
		this.range=maxValue-minValue;
		this.maxPeakFromLast=maxPeakFromLast;
		this.timeForCapture=timeForCapture;
		this.random=new Random();
		
		if (pattern_duration.isPresent())
			this.pattern_duration=Optional.ofNullable(pattern_duration.get());
		this.status=SENSOR_STATUS.READY;
		this.pattern=pattern;
		this.startValue=startValue;
		this.detectClassType();
		this.setupDetailsInfo();
		if (startValue instanceof Double || startValue instanceof Vector3)
			lastValue=Optional.ofNullable(this.startValue);
		else
			throw new Exception("The Type T is wrong");
		if (pattern==SENSOR_SIMULATION_PATTERN.QUADRATIC)
			setupFlatValue();
	}
	
	protected void calculatePhase() throws Exception
	{
		switch(this.sensorGeneric)
		{
		case SCALAR:
			phase=((Double)this.lastValue.get())/this.maxValue;
			break;
		case VECTOR3:
			Vector3 tempValue=(Vector3) this.lastValue.get();
			phase=new Vector3(tempValue.x/this.maxValue,tempValue.y/this.maxValue,tempValue.z/this.maxValue);
			break;
		default:
			throw new Exception("It's possible to calculate phase only for SCALAR or VECTOR3 sensors");
		}
	}
	
	private void setupDetailsInfo()
	{
		this.details.addDetail(SENSOR_DETAIL.MIN_VALUE,this.minValue);
		this.details.addDetail(SENSOR_DETAIL.MAX_VALUE,this.maxValue);
		this.details.addDetail(SENSOR_DETAIL.DELAY,this.timeForCapture);
	}
	
	private void detectClassType()
	{
		if (this.startValue instanceof Double)
		{
			this.sensorGeneric=SENSOR_GENERIC.SCALAR;
		}
		else if (this.startValue instanceof Vector3)
			this.sensorGeneric=SENSOR_GENERIC.VECTOR3;
			
	}
	
	
	public SENSOR_STATUS getStatus()
	{
		return this.status;
	}

	
	@Override
	public abstract void run();
	
	@SuppressWarnings("unchecked")
	protected void updateSinusoidal() {
		double timeLapsed=((double)this.t2-(double)this.time_start)/1000;
		
		if (lastValue.get() instanceof Vector3)
		{
			Vector3 nextPhase=new Vector3(
					Math.cos(((Vector3)phase).x+pulse*timeLapsed),
					Math.cos(((Vector3)phase).y+pulse*timeLapsed),
					Math.cos(((Vector3)phase).z+pulse*timeLapsed)
					);
			
			this.lastValue=(Optional<T>)Optional.ofNullable(new Vector3(
					this.minValue+range/2 + range*nextPhase.x/2,
					this.minValue+range/2 + range*nextPhase.y/2,
					this.minValue+range/2 + range*nextPhase.z/2
					));
		}
		else 
		{

			double nextPhase=Math.cos((double)phase+pulse*timeLapsed);
			this.lastValue=(Optional<T>) Optional.ofNullable(new Double(this.minValue+(range/2)+(range/2)*nextPhase));
		}
		
	}
	
	private void setupFlatValue()
	{
		
		interval=(this.maxValue-this.minValue)/3;
		flatNegative=this.minValue+interval;
		flatPositive=flatNegative+interval;
	}
	
	private Double getFlat(Double value)
	{
		if (value<=this.flatNegative)
			return this.minValue;
		else if (value>this.flatNegative&&value<=this.flatPositive)
			return new Double(0);
		
		return this.maxValue;
	}
	
	private Vector3 getFlat(Vector3 vector)
	{
		return new Vector3(getFlat(vector.x),getFlat(vector.y),getFlat(vector.z));
	}


	@SuppressWarnings("unchecked")
	protected void updateQuadratic() {
		
		updateSinusoidal();
		
		if (lastValue.get() instanceof Vector3)
		{
			Vector3 peak=Vector3.random(this.maxPeakFromLast);
			Vector3 flatLastVector=getFlat((Vector3)this.lastValue.get());
			this.lastValue=(Optional<T>)Optional.ofNullable(new Vector3(
					Vector3.rebound(flatLastVector.x+peak.x,this.minValue,this.maxValue),
					Vector3.rebound(flatLastVector.y+peak.y,this.minValue,this.maxValue),
					Vector3.rebound(flatLastVector.z+peak.z,this.minValue,this.maxValue))
					);
			
		}
		else
		{
			Double peak=new Double(random.nextDouble()*this.maxPeakFromLast*2-this.maxPeakFromLast);
			this.lastValue=(Optional<T>)Optional.ofNullable(Vector3.rebound(getFlat((Double)this.lastValue.get())+peak,this.minValue,this.maxValue));
			
		}
		
	}

	//RANDOM UPDATE
	@SuppressWarnings("unchecked")
	protected void updateRandom() {

		if (lastValue.get() instanceof Vector3)
		{
			Vector3 increment=Vector3.random(this.maxPeakFromLast);
			lastValue=(Optional<T>) Optional.ofNullable(Vector3.incrementVector((Vector3)lastValue.get(), increment, minValue, maxValue));
		}
		else
		{
			Double increment=new Double(random.nextDouble()*this.maxPeakFromLast*2-this.maxPeakFromLast);
			lastValue=(Optional<T>) Optional.ofNullable(Vector3.rebound((Double)lastValue.get()+increment, minValue, maxValue));
		}
	}
	
	
	
	@Override
	public final String toString() {
		return String.format("[%s] MIN:%lf | MAX:%lf | DELAY: %d ", getSensorType().toString(),minValue,maxValue,this.timeForCapture);
	}
}
