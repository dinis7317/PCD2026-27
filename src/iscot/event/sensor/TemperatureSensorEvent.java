package iscot.event.sensor;

import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {
	public final float temperature; // always celsius

	public TemperatureSensorEvent(Areas area, float valor) {
		super(area);
		this.temperature = valor;
	}
	@Override
	public String toString() {
		return "TemperatureSensorEvent [temperature=" + temperature + ", area=" + area + ", getType()=" + getType() + "]";
	}
}
