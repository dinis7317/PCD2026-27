package iscot.event.sensor;

import iscot.enums.Areas;

public class LightSensorEvent extends SensorEvent {
	public final float light; // always in lux

	public LightSensorEvent(Areas area, float light) {
		super(area);
		this.light = light;
	}

	@Override
	public String toString() {
		return "LightSensorEvent [light=" + light + ", area=" + area + ", getType()=" + getType() + "]";
	}

}
