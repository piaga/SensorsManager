package controller.spring.handlers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import controller.spring.WebSocketController;
import controller.spring.observers.SensorsManagerObserver;
import model.manager.SensorsManager;

@Component
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent>{
	
	@Autowired
	SensorsManagerObserver observer;
	
	@Autowired
	SensorsManager manager;
	
	@Autowired
	WebSocketController controller;
	

	private int counter;
	private ArrayList<String> subscribers=new ArrayList<String>();
	
	

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		log(event.getClass().getSimpleName());
		StompHeaderAccessor headerAccessor;
		if (event instanceof SessionConnectedEvent)
		{
			SessionConnectedEvent scEvent=(SessionConnectedEvent) event;
			
			headerAccessor = StompHeaderAccessor.wrap(scEvent.getMessage());
			subscribers.add(headerAccessor.getSessionId());
			
			
			
		}
		else if (event instanceof SessionDisconnectEvent)
		{
			SessionDisconnectEvent sdEvent=(SessionDisconnectEvent) event;
			headerAccessor=StompHeaderAccessor.wrap(sdEvent.getMessage());
			if (subscribers.contains(headerAccessor.getSessionId()))
			{
				subscribers.remove(headerAccessor.getSessionId());
				if (subscribers.isEmpty())
					observer.unsubscribe();
				
			}
		}
		if (event instanceof SessionSubscribeEvent)
		{
			SessionSubscribeEvent ssEvent=(SessionSubscribeEvent) event;
			headerAccessor=StompHeaderAccessor.wrap(ssEvent.getMessage());
			String url=headerAccessor.getDestination();
			log("Subscription to "+url);
			switch(url)
			{
			case "/sensors/status":
				break;
			case "/sensors/details":
				controller.sendSensorsDetails(manager.getSensorsDetails());
				break;
			case "/sensors/update":
				if(!observer.isSubscribed())
				{
					observer.subscribe();
				}
				break;
				default: break;
			}
		}
		log(String.format("There are %d subscribers", subscribers.size()));

	}
	
	private void log(String message)
	{
		System.out.println("[ApplicationEventListener] "+message);
	}

}
