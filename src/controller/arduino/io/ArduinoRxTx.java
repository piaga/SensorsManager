package controller.arduino.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import scala.NotImplementedError;
import java.util.stream.*;
import java.util.Arrays;
import java.util.Enumeration;

public class ArduinoRxTx implements SerialPortEventListener {

	SerialPort serialPort;
	
	
	private static final String PORT_NAMES[] = {
			"/dev/ttyACM0",
			"COM3"
	};
	
	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE=9600;
	
	public ArduinoRxTx() {
		
	}
	
	
	
	private boolean init()
	{
		System.setProperty("gnu.io.rtxt.SerialPorts","/dev/ttyACM0");
		CommPortIdentifier portId=null;
		
		Enumeration<CommPortIdentifier> portEnum=CommPortIdentifier.getPortIdentifiers();
		while(portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId=(CommPortIdentifier) portEnum.nextElement();
			
			for (String port_name : PORT_NAMES) {
				if (currPortId.getName().equals(port_name))
				{
					portId=currPortId;
					break;
				}
			}
		};
		
		if (portId==null)
		{
			System.err.println("Could not find COM port");
			return false;
		}
		try
		{
			serialPort= (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream(), Charset.defaultCharset()));
			output = serialPort.getOutputStream();
			
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			return true;
			
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			return false;
			
		}
	}
	
	public synchronized void serialEvent(SerialPortEvent oEvent)
	{
		switch(oEvent.getEventType())
		{
			case SerialPortEvent.DATA_AVAILABLE:
			{
				
			}
			
		default:
			throw new NotImplementedError();
				
		}
	}

}
