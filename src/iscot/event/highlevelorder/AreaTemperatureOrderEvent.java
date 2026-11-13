package iscot.event.highlevelorder;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent {

	private final AirConditioningMode modo;
	private final float temperatura;

	public AreaTemperatureOrderEvent(Areas area, AirConditioningMode modo, float temperatura) {
		super(area);
		this.modo = modo;
		this.temperatura = temperatura;
	}

	public AirConditioningMode getModo() {
		return modo;
	}

	public float getTemperatura() {
		return temperatura;
	}

	@Override
	public String toString() {
		return "AreaTemperatureOrderEvent {area=" + getArea() + ", modo=" + modo + ", temperatura=" + temperatura + " °C, topico=" + getType() + "}";
	}
}
