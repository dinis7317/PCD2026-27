package iscot.event.highlevelorder;

import iscot.enums.AirConditioningMode;
import iscot.enums.Areas;

public class AreaTemperatureOrderEvent extends HighLevelOrderEvent{
	public AreaTemperatureOrderEvent(Areas area, AirConditioningMode mode, float temperature) {
		super(area);
		this.mode = mode;
		this.temperature = temperature;
	}
	@Override
	public String toString() {
		return "AreaTemperatureOrderMessage [mode=" + mode + ", temperature=" + temperature + ", area=" + area + "]";
	}
	public final AirConditioningMode mode;
	public final float temperature;

}
