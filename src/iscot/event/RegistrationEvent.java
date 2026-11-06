package iscot.event;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {
	public final Type type;
	public RegistrationEvent(Areas area, Type type) {
		super(area);
		this.type = type;
	}
	@Override
	public Type getType() {
		return type;
	}
	
}
