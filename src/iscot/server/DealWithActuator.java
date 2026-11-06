package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.sensor.LightSensorEvent;

public class DealWithActuator extends DealWithClient {

	public DealWithActuator(ObjectInputStream in, ObjectOutputStream out, Server server, Areas area) {
		super( server, area, in, out);
	}

	@Override
	public void run() {
		while(true)
			try {
				// TODO: deal with other deciders
				Event message=server.getMessage(Type.DECIDER, getArea(),getClientId());
				System.err.println(this+" got: "+message);
				out.writeObject(message);
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public Type getType() {
		return Type.ACTUATOR;
	}

	@Override
	public String toString() {
		return "DealWithActuator [getType()=" + getType() + ", getArea()=" + getArea() + ", getClientId()="
				+ getClientId() + "]";
	}
}
