package iscot.event;

import java.awt.geom.Area;
import java.io.Serializable;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Event implements Serializable {
	@Override
	public String toString() {
		return "Message [area=" + area + " type:"+ getType()+"]";
	}

	public final Areas area;
	
	public abstract Type getType();

	public Event(Areas area) {
		this.area = area;
	}
}
