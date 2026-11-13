package iscot.event.sensor;

import iscot.enums.Areas;

public class HumiditySensorEvent extends SensorEvent {

	public HumiditySensorEvent(Areas area, float valor) {
		super(area, valor);
	}

	@Override
	public String toString() {
		return "HumiditySensorEvent {area=" + getArea() + ", valor=" + getValor() + " %, topico=" + getType() + "}";
	}
}
