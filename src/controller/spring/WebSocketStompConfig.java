package controller.spring;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import controller.spring.handlers.HttpHandshakeInterceptor;
import controller.spring.observers.SensorsManagerObserver;
import model.manager.SensorsManager;
import model.sensors.DefectedRealVirtualSensor;
import model.sensors.WorkingCoordinatesVirtualSensor;
import model.sensors.WorkingDependantRealVirtualSensor;
import model.sensors.WorkingImageVirtualSensor;
import model.sensors.WorkingRealVirtualSensor;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.GeographicCoordinates;
import model.sensors.structures.Vector3;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
	@Value("${sensor.timescale:1}")
	private double timeScale;
	
	@Value("${sensor.displaymap:false}")
	private boolean displayMap;
	@Value("${sensor.displayimage:false}")
	private boolean displayImage;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config)
	{
		config.enableSimpleBroker("/sensors","/queue");
		config.setApplicationDestinationPrefixes("/app");
		
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry arg0) {
		
		arg0.addEndpoint("/greetings").setAllowedOrigins("*").addInterceptors(new HttpHandshakeInterceptor()).withSockJS();
		
	}
	
	
	
	@Bean
	
	public SensorsManager getManager() throws Exception
	{
		if (this.timeScale<0.2)
			this.timeScale=0.2;
		else if (this.timeScale>1)
			this.timeScale=1;
		
		SensorsManager manager=SensorsManager.getSensorsObserver((new Random()).nextInt(), Optional.empty());
		if (manager.getSensorsList().size()==0)
		{
			
			manager.addSensor(new WorkingRealVirtualSensor<Vector3>(SensorsManager.getNewId(),
					new Vector3((double)20,(double)0,(double)-20), -20, 20, 2, (int) (62/timeScale), SENSOR_TYPE.ACCELEROMETER, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, Optional.ofNullable(120)));
			manager.addSensor(new WorkingRealVirtualSensor<Vector3>(SensorsManager.getNewId(),
					new Vector3((double)1,(double)0,(double)-1), -1, 1,(double) 0.0003,(int) (62/timeScale), SENSOR_TYPE.ORIENTATION, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, Optional.ofNullable(120)));
			
			if (this.displayMap) manager.addSensor(new WorkingCoordinatesVirtualSensor(SensorsManager.getNewId(),Optional.ofNullable(new GeographicCoordinates(44.137965, 12.243692)), 5f,(int) (200/timeScale), SENSOR_TYPE.GPS));
			if (this.displayImage)
			{
				manager.addSensor(new WorkingImageVirtualSensor(SensorsManager.getNewId(), SENSOR_TYPE.CAMERA,(int) (62/timeScale)));
				manager.addSensor(new WorkingRealVirtualSensor<Double>(SensorsManager.getNewId(),
						(double) 600, 300, 600, 50,(int) (310/timeScale), SENSOR_TYPE.LIGHT, SENSOR_SIMULATION_PATTERN.QUADRATIC, Optional.ofNullable(8)));
				//manager.addSensor((new WorkingDependantRealVirtualSensor<Double>(SensorsManager.getNewId(), SENSOR_TYPE.LIGHT, manager.getSensorByType(SENSOR_TYPE.CAMERA).get())));
			
			}
			manager.addSensor(new DefectedRealVirtualSensor<Double>(SensorsManager.getNewId(),
					(double)10,(double) 0,(double) 40,(double) 20, 200,(int)( 1200/timeScale),(int)( 50/timeScale), SENSOR_TYPE.PRESSURE, SENSOR_SIMULATION_PATTERN.QUADRATIC, Optional.ofNullable(5)));
			manager.addSensor(new WorkingRealVirtualSensor<Double>(SensorsManager.getNewId(),(double) 100,(double) 0,(double) 100,(double) 0,(int) (2000/timeScale), SENSOR_TYPE.BATTERY, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, Optional.ofNullable(100)));
			manager.addSensor(new WorkingRealVirtualSensor<Double>(SensorsManager.getNewId(),(double) 100,(double)0,(double)100,(double)3,(int)(124/timeScale),SENSOR_TYPE.PROXIMITY,SENSOR_SIMULATION_PATTERN.SINUSOIDAL,Optional.ofNullable(100)));
			manager.addSensor(new WorkingRealVirtualSensor<Double>(SensorsManager.getNewId(),new Double(20),new Double(10),new Double(80),new Double(1),(int)(368/timeScale),SENSOR_TYPE.TEMPERATURE,SENSOR_SIMULATION_PATTERN.RANDOM,Optional.empty()));
			
		}
		return manager;
	}
	
	/*@Bean
	public SensorManagerObserver getObserver()
	{
		return new SensorManagerObserver();
	}*/

}
