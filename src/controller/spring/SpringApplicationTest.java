package controller.spring;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.gson.Gson;

import controller.spring.handlers.ApplicationEventListener;
import controller.spring.observers.SensorsManagerObserver;
import model.manager.SensorsManager;

@SpringBootApplication
public class SpringApplicationTest extends SpringBootServletInitializer {
	
	
	public static void main(String[] args) throws Exception
	{
		//TO RUN
		ConfigurableApplicationContext context = SpringApplication.run(SpringApplicationTest.class, args);
		
		
		//CONFIG
		/*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(WebSocketConfig.class);
		ctx.refresh();
		SensorsManager manager=ctx.getBean(SensorsManager.class);
		SensorManagerObserver observer=ctx.getBean(SensorManagerObserver.class);
		observer.setManager(manager);
		ctx.close();
		*/
		
		
	}
}
