package model.sensors.tests;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;

import javax.imageio.ImageIO;

import model.sensors.structures.Image;

public class BufferedImageTest {

	public static void main(String[] args) throws Exception {
		
		ImageIO.setUseCache(true);
		String encoded="";
		Thread.sleep(1000);
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();;
		GraphicsConfiguration gc=ge.getDefaultScreenDevice().getDefaultConfiguration();
		long t1,t2,t3,t4,t5,t6;
		for (int i=1;i<100; i++)
		{
			t1=System.nanoTime();
			Image image=new Image(Optional.ofNullable(1920),Optional.ofNullable(1080),Optional.empty());
			t2=System.nanoTime();
			image.setIntensity(50);
			t3=System.nanoTime();
			image=image.writeOnImage("Something for ya!");
			t4=System.nanoTime();
			BufferedImage bImage=image.getBufferedImage();
			t5=System.nanoTime();
			encoded=image.getImageToBase64();
			
			
			t6=System.nanoTime();
			
			long used=t6-t1;
			
			Double p1=(Double.sum(t2, -t1)/used)*100,p2=(Double.sum(t3, -t2)/used)*100,p3=(Double.sum(t4, -t3)/used)*100,p4=(Double.sum(t5, -t4)/used)*100,p5=(Double.sum(t6, -t5)/used)*100;
			System.out.println("Attemp "+i);
			System.out.println("Time to processing in ms: "+(t6-t1)/1000000);
			System.out.println("% of creation: "+p1.toString()+" | progressive: "+(t2-t1)/1000000);
			System.out.println("% of change intensity: "+p2+" | progressive: "+(t3-t1)/1000000);
			System.out.println("% of writing: "+p3+" | progressive: "+(t4-t1)/1000000);
			System.out.println("% of buffering: "+p4+" | progressive: "+(t5-t1)/1000000);
			System.out.println("% of encoding: "+p5+" | progressive: "+(t6-t1)/1000000);
			
		}
		System.out.println("Encoded: "+encoded);
		
		

	}

}
