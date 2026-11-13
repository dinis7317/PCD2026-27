package iscot.event.sensor;

import iscot.enums.Areas;

public class GroundMoistureSensorEvent extends SensorEvent {

	public GroundMoistureSensorEvent(Areas area, float valor) {
		super(area, valor);
	}

	@Override
	public String toString() {
		return "GroundMoistureSensorEvent {area=" + getArea() + ", valor=" + getValor() + " %, topico=" + getType() + "}";
	}
}
