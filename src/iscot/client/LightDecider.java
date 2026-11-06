package iscot.client;


import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iscot.client.gui.LightIcon;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.RegistrationEvent;
import iscot.event.highlevelorder.AreaLightingOrderEvent;
import iscot.event.sensor.LightSensorEvent;

public class LightDecider {

	/**
	 * 
	 * @param args server address and port
	 * @param args roomName: string
	 * @param args lightThreshold: int
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		if(args.length!=4) {
			System.err.println("Invalid arguments!");
		}
		else {
			Socket socket=new Socket(args[0],Integer.parseInt(args[1]));
			ObjectOutputStream out= new ObjectOutputStream(socket.getOutputStream()); 
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			// register with the server
			Areas area=Areas.valueOf(args[2]);
			out.writeObject(new RegistrationEvent(area, Type.DECIDER));

			int lightThreshold = Integer.parseInt(args[3]);

			while(true) {
				Event message=(Event) in.readObject();
				if( message instanceof LightSensorEvent) {
					LightSensorEvent lightSensorMessage=(LightSensorEvent)message;
					if(lightSensorMessage.area == area)
						if(lightSensorMessage.light<lightThreshold) {
							out.writeObject(new AreaLightingOrderEvent(area, true));
							System.err.println("Ordering light on at:"+area);
						}
						else {
							out.writeObject(new AreaLightingOrderEvent(area, false));
							System.err.println("Ordering light off at:"+area);
						}
					else
						System.err.println("Message for unexpected area!");
				}
				else
					System.err.println("Unexpected message in Light:"+message);

			}
		}
	}
}
