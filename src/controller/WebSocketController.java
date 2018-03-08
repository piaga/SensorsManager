package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.manager.SensorsManager;
import model.manager.structures.EntrySet;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.Vector3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import controller.observers.SensorsManagerObserver;

@Controller
public class WebSocketController {
	
	@Autowired
	SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	SensorsManager manager;
	
	@Autowired
	SensorsManagerObserver observer;
	
	
	public WebSocketController() throws Exception
	{
		System.out.println("I'm a websocketController");
		
	}
	
	
	
	@MessageMapping("/message")
	public void processMessageFromClient(@Payload String message, SimpMessageHeaderAccessor headerAccessor) throws Exception{
		
		String sessionId=headerAccessor.getSessionId();
		
		System.out.println(sessionId);
		String command=(String) new Gson().fromJson(message, Map.class).get("name");
		headerAccessor.setSessionId(sessionId);
		System.out.println(sessionId+" "+command);
		if (command.contains("CLOSE"))
		{
			manager.disconnectObserver(observer);
		}
		//messagingTemplate.convertAndSend("/topic/reply",new Gson().fromJson(message, Map.class).get("name"));
		
		//messagingTemplate.convertAndSend("/topic/reply",message);
		
	}
	
	public void sendUpdate(EntrySet entry)
	{
		messagingTemplate.convertAndSend("/sensors/update",new Gson().toJson(entry));
		
	}
	
	public void sendSensorsDetails(ArrayList<EntrySet> sensorsDetails)
	{
		
		for(EntrySet entry : sensorsDetails)
		{
			System.out.println(new Gson().toJson(entry));
			messagingTemplate.convertAndSend("/sensors/details",new Gson().toJson(entry));
			
		}
		
	}

	public void sendStatus(EntrySet entry) {

		messagingTemplate.convertAndSend("/sensors/status",new Gson().toJson(entry));
	}
	
	

}
