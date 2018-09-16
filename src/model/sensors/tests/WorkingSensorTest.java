package model.sensors.tests;

import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.structures.EntrySet;
import model.sensors.*;
import model.sensors.enumerators.*;
import model.sensors.structures.Vector3;
import model.sensors.virtual.WorkingRealVirtualSensor;


public class WorkingSensorTest {
	
	public static void main(String[] args) throws Exception {
		int count=10;
		ISensor sensor=new WorkingRealVirtualSensor<Vector3>(java.util.UUID.randomUUID(),new Vector3(),-20, 50, 2, 10,
				SENSOR_TYPE.ACCELEROMETER, SENSOR_SIMULATION_PATTERN.SINUSOIDAL , Optional.ofNullable(4));
		
		Observer observer=new Observer<EntrySet>() {
			Disposable disposable;
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				System.out.println("Observer closed");
				disposable.dispose();
			}
			
			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNext(EntrySet arg0) {
				System.out.println(arg0.getObject());
				if (arg0.getObject() instanceof Double)
					System.out.println((Vector3)arg0.getObject());
				
			}

			@Override
			public void onSubscribe(Disposable arg0) {
				// TODO Auto-generated method stub
				System.out.println("Observer subscribed");
				disposable=arg0;

			}
		};
		
		 sensor.getObservable().subscribe(observer);
		
		sensor.start();
		while(count>=0)
		{
			
			
			Thread.sleep(1000);
			count--;
		}
		observer.onComplete();
		
	}

}
