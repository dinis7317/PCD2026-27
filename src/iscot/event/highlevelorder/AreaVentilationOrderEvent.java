package iscot.event.highlevelorder;

import iscot.enums.Areas;

public class AreaVentilationOrderEvent extends HighLevelOrderEvent{
	@Override
	public String toString() {
		return "AreaVentilationOrderMessage [state=" + state + ", area=" + area + ", getType()=" + getType() + "]";
	}

	public final boolean state;

	public AreaVentilationOrderEvent(Areas area, boolean state) {
		super(area);
		this.state = state;
	}
}
