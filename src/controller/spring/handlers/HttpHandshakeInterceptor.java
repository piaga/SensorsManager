package controller.spring.handlers;

import java.time.LocalDateTime;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.google.gson.Gson;

import model.manager.SensorsManager;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {
	
	

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Exception exception) {
		
		
		
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler,
			Map<String, Object> attributes) throws Exception {
		
		if (request instanceof ServletServerHttpRequest)
		{
			ServletServerHttpRequest servletRequest=(ServletServerHttpRequest) request;
			HttpSession session=servletRequest.getServletRequest().getSession();
			System.out.println(new Gson().toJson(session));
			attributes.put("sessionid", session.getId());
			//attributes.put("sensorsDetails", new Gson().toJson(manager.getSensorsDetails()));
			
		}
		return true;
	}

}
