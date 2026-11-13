package iscot.event.sensor;

import iscot.enums.Areas;

public class TemperatureSensorEvent extends SensorEvent {

	public TemperatureSensorEvent(Areas area, float valor) {
		super(area, valor);
	}

	@Override
	public String toString() {
		return "TemperatureSensorEvent {area=" + getArea() + ", valor=" + getValor() + " °C, topico=" + getType() + "}";
	}
}
