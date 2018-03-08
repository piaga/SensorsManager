package model.sensors.structures;

import java.awt.Color;
import java.awt.Graphics;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.util.Optional;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;









public class Image {
	
	
	private BufferedImage image;
	private ByteArrayOutputStream os = new ByteArrayOutputStream();
	private final int minRectW,minRectH;
	private final double squareWidthStep,squareHeightStep;
	
	
	public Image(BufferedImage image)
	{

		this.image=image;
		this.minRectW=image.getWidth()/10;
		this.minRectH=image.getHeight()/10;
		this.squareWidthStep=(double)((image.getWidth()-this.minRectW)/(double)255);
		this.squareHeightStep=(double)((image.getHeight()-this.minRectH)/(double)255);
		
	}
	
	public Image(Optional<Integer> width,Optional<Integer> height,Optional<Integer> imageType) {
		image=new BufferedImage(width.isPresent()?width.get():600, height.isPresent()?height.get():400, imageType.isPresent()?imageType.get():BufferedImage.TYPE_3BYTE_BGR);
		this.minRectW=image.getWidth()/10;
		this.minRectH=image.getHeight()/10;
		this.squareWidthStep=(double)(image.getWidth()-this.minRectW)/(double)255;
		this.squareHeightStep=(double)(image.getHeight()-this.minRectH)/(double)255;
		
		
	}
	
	
	
	public BufferedImage getBufferedImage() {
		return image;
	}
	
	
	
	public void setIntensity(int value)
	{
		Graphics g=image.getGraphics();
		g.setColor(new Color(value, value, value));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.WHITE);
		g.drawLine(0, 0, (image.getWidth()/2)-this.minRectW/2, (image.getHeight()/2)-this.minRectH/2);
		g.drawLine(image.getWidth(), 0, (image.getWidth()/2)+this.minRectW/2, (image.getHeight()/2)-this.minRectH/2);
		g.drawLine(0, image.getHeight(), (image.getWidth()/2)-this.minRectW/2, (image.getHeight()/2)+this.minRectH/2);
		g.drawLine(image.getWidth(), image.getHeight(), (image.getWidth()/2)+this.minRectW/2, (image.getHeight()/2)+this.minRectH/2);
		g.drawRect((image.getWidth()/2)-this.minRectW/2, (image.getHeight()/2)-this.minRectH/2, this.minRectW,this.minRectH);
		g.dispose();
	}
	
	public Image writeOnImage(String message)
	{
		BufferedImage imageClean=image;
		Graphics g=imageClean.getGraphics();
		g.setFont(g.getFont().deriveFont(30f));
		g.drawString(message, 10, 40);
		g.dispose();
		
		return new Image(imageClean);
	}
	
	public void drawSquare(int step)
	{
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		
		g.drawRect((int)(this.image.getWidth()/2-this.squareWidthStep*step/2-this.minRectW/2),
				(int) (this.image.getHeight()/2-this.squareHeightStep*step/2 - this.minRectH/2),
				(int)(this.minRectW + this.squareWidthStep*step),
				(int)(this.minRectH + this.squareHeightStep*step));
		g.dispose();
	}
	
	public String getImageToBase64() throws Exception
	{
		

		
		
		os.reset();
		ImageIO.write(image,"jpg",os);
		String outputString=Base64.getEncoder().encodeToString(os.toByteArray());
		os.flush();
		return outputString;
		
	}
	
	public String getImageToBase64(Image image) throws IOException
	{
		
		os.reset();
		ImageIO.write(image.getBufferedImage(),"jpg",os);
		String outputString=Base64.getEncoder().encodeToString(os.toByteArray());
		os.flush();
		return outputString;
		
		
		

		
		
	}
	
	
	
	public static byte[] imageToByteArray(Image image) throws Exception
	{
		
		byte[] imageBytes = ((DataBufferByte) image.getBufferedImage().getData().getDataBuffer()).getData();
		return imageBytes;
	}
	
	
}
