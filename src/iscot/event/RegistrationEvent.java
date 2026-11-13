package iscot.event;

import iscot.enums.Areas;
import iscot.enums.Type;

public class RegistrationEvent extends Event {

	private final Type tipoCliente;

	public RegistrationEvent(Areas area, Type tipoCliente) {
		super(area);
		this.tipoCliente = tipoCliente;
	}

	@Override
	public Type getType() {
		return tipoCliente;
	}

	@Override
	public String toString() {
		return "RegistrationEvent {area=" + getArea() + ", tipo=" + tipoCliente + "}";
	}
}
