package iscot.event.highlevelorder;

import iscot.enums.Areas;

public class AreaLightingOrderEvent extends HighLevelOrderEvent {
	@Override
	public String toString() {
		return "AreaLightingOrderMessage [state=" + state + ", area=" + area + ", getType()=" + getType() + "]";
	}

	public final boolean state;

	public AreaLightingOrderEvent(Areas area, boolean state) {
		super(area);
		this.state = state;
	}
}
