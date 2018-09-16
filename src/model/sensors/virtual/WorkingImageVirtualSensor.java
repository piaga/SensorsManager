package model.sensors.virtual;


import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.UUID;

import model.manager.structures.EntrySet;
import model.sensors.AbstractSensor;
import model.sensors.enumerators.SENSOR_DETAIL;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;
import model.sensors.structures.Image;

public class WorkingImageVirtualSensor extends AbstractSensor<Object> {

	private int delay,frames=0;
	private long time_start,t1,t2;
	private Image image;
	
	public WorkingImageVirtualSensor(UUID id, SENSOR_TYPE sensorType,int delay) {
		super(id, sensorType);
		this.delay=delay;
		image=new Image(Optional.empty(), Optional.empty(), Optional.empty());
		this.details.addDetail(SENSOR_DETAIL.DELAY, delay);
		this.details.addDetail(SENSOR_DETAIL.RESOLUTION, String.format("[%dx%d]", this.image.getBufferedImage().getWidth(),this.image.getBufferedImage().getHeight()));
		this.details.addDetail(SENSOR_DETAIL.COLOR_DEPTH, BufferedImage.TYPE_INT_RGB);
		setStatus(SENSOR_STATUS.READY);
	}

	@Override
	public void run() {
		this.time_start=System.currentTimeMillis();
		this.t2=this.time_start;
		this.log("Running");
		try
		{
			
			while(this.status==SENSOR_STATUS.RUNNING)
			{
				this.t1=System.currentTimeMillis();
				
				if(!updateObserver(new EntrySet(sensorType,
						image.getImageToBase64(image.writeOnImage(""+frames))
						//Image.imageToByteArray(Image.writeOnImage(image,""+frames))
						))) break;
				frames++;
				image.setIntensity(frames%255);
				image.drawSquare((frames*5)%255);
				
				
				
				
				
				t2=System.currentTimeMillis();
				//log(String.format("(Frame %d) Time to process: %d", frames,t2-t1));
				if ((t2-t1)<delay)
					Thread.sleep((delay-(t2-t1)));
				
			}
			log("Running complete!");
			setStatus(SENSOR_STATUS.READY);
			
			
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			setStatus(SENSOR_STATUS.DAMAGED);
		}

	}

}
