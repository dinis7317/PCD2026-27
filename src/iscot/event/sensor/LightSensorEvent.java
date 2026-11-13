package iscot.event.sensor;

import iscot.enums.Areas;

public class LightSensorEvent extends SensorEvent {

	public LightSensorEvent(Areas area, float valor) {
		super(area, valor);
	}

	@Override
	public String toString() {
		return "LightSensorEvent {area=" + getArea() + ", valor=" + getValor() + " lux, topico=" + getType() + "}";
	}
}
