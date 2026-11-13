package iscot.event.highlevelorder;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;

public abstract class HighLevelOrderEvent extends Event {

	protected HighLevelOrderEvent(Areas area) {
		super(area);
	}

	@Override
	public Type getType() {
		return Type.DECIDER;
	}
}
