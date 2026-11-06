package iscot.event.highlevelorder;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {
	public final boolean state;

	public AreaIrrigationOrderEvent(Areas area, boolean state) {
		super(area);
		this.state = state;
	}

	@Override
	public String toString() {
		return "AreaIrrigationrderMessage [state=" + state + ", area=" + area + "]";
	}
}
