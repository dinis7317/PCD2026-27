package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.RegistrationEvent;

public class DealWithClient extends Client{
	public DealWithClient(Server server, Areas area, ObjectInputStream in, ObjectOutputStream out) {
		super(server, area);
		
		this.in=in;
		this.out=out;
	}
	protected ObjectInputStream in;
	protected ObjectOutputStream out;
	
	
	@Override
	public void run() {
		// get registration
		try {
			RegistrationEvent registrationMessage=(RegistrationEvent)in.readObject();
			
			switch (registrationMessage.type) {
			case SENSOR:
					DealWithSensor dealWithSensor=new DealWithSensor(in, out,server,registrationMessage.area);
					server.register(dealWithSensor);
					dealWithSensor.start();
				break;
			case DECIDER:
				DealWithDecider dealWithDecider=new DealWithDecider(in, out,server,registrationMessage.area);
				server.register(dealWithDecider);
				dealWithDecider.start();
				break;
			case ACTUATOR:
				DealWithActuator dealWithActuator= new DealWithActuator(in, out, server, registrationMessage.area);
				server.register(dealWithActuator);
				dealWithActuator.start();
				break;
			default:
				System.err.println("Unexpected client registration:"+registrationMessage);
				break;
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


//	@Override
//	public  String toString() {
//		return "DealWithClient [getArea()=" + getArea() + ", getClientId()=" + getClientId() + "]";
//	}


	@Override
	public Type getType() {
		return Type.DWC;
	}

}
