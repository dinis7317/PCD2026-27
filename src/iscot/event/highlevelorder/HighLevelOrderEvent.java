package iscot.event.highlevelorder;

import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;

public abstract class HighLevelOrderEvent extends Event {

	public HighLevelOrderEvent(Areas area) {
		super(area);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.DECIDER;
	}

}
