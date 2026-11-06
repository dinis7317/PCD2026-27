package iscot.event.sensor;

import iscot.enums.Areas;

public class GroundMoistureSensorEvent extends SensorEvent{
	public final float humidity; // always in %

	public GroundMoistureSensorEvent(Areas area, float humidity) {
		super(area);
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "GroundMoistureSensorMessage [humidity=" + humidity + ", area=" + area + ", getType()=" + getType() + "]";
	}
}
