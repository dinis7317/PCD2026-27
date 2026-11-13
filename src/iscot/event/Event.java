package iscot.event;

import java.io.Serializable;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Event implements Serializable {

	private final Areas area;

	protected Event(Areas area) {
		this.area = area;
	}

	public Areas getArea() {
		return area;
	}

	public abstract Type getType();
}
