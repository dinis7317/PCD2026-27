package iscot.event.sensor;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;

public abstract class SensorEvent extends Event {

	@Override
	public Type getType() {
		return Type.SENSOR;
	}

	public SensorEvent(Areas area) {
		super(area);
	}

}
