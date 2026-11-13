package iscot.event.sensor;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;

public abstract class SensorEvent extends Event {

	private final float valor;

	protected SensorEvent(Areas area, float valor) {
		super(area);
		this.valor = valor;
	}

	public float getValor() {
		return valor;
	}

	@Override
	public Type getType() {
		return Type.SENSOR;
	}
}
