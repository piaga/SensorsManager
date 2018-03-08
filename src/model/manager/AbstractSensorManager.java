package model.manager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.structures.DataSet;
import model.manager.structures.EntrySet;

public abstract class AbstractSensorManager extends Observable<EntrySet> implements Observer<EntrySet> {

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
	
}
