package iscot.event.sensor;

import iscot.enums.Areas;

public class HumiditySensorEvent extends SensorEvent{
	public final float humidity; // always in %

	public HumiditySensorEvent(Areas area, float humidity) {
		super(area);
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "HumiditySensorMessage [light=" + humidity + ", area=" + area + ", getType()=" + getType() + "]";
	}

}
