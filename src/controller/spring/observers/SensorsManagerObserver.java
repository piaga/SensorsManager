package controller.observers;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;


import controller.WebSocketController;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.*;
import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;


@Component
public class SensorsManagerObserver implements Observer<Object>{
	
	@Autowired
	private WebSocketController controller;
	
	
	
	@Autowired
	private SensorsManager manager;
	
	private boolean subscribed=false;
	
	
	public boolean isSubscribed()
	{
		return this.subscribed;
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		//unsubscribe();
		
	}
	
	public void subscribe()
	{
		System.out.println("[Observer] Subscribing" );
		
		manager.subscribe(this);
		subscribed=true;
		System.out.println("[Observer] Subscribed" );
	}
	
	public void unsubscribe()
	{
		System.out.println("[Observer] Unsubscribing" );
		manager.disconnectObserver(this);
		subscribed=false;
		System.out.println("[Observer] Unsubscribed" );
	}

	@Override
	public void onError(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNext(Object object) {
		
		if (object instanceof EntrySet)
		{
			
			EntrySet entry=(EntrySet) object;
			if (entry.getObject() instanceof SENSOR_STATUS)
				
				controller.sendStatus(entry);
			else if (entry.getObject() instanceof byte[])
			{
				controller.sendUpdate(entry);
				
			}
			else
				
				controller.sendUpdate(entry);
		}
		
		
	}

	@Override
	public void onSubscribe(Disposable arg0) {
		
		
	}
	
	
}
