package iscot.event.highlevelorder;

import iscot.enums.Areas;

public class AreaIrrigationOrderEvent extends HighLevelOrderEvent {

	private final boolean ligado;

	public AreaIrrigationOrderEvent(Areas area, boolean ligado) {
		super(area);
		this.ligado = ligado;
	}

	public boolean isLigado() {
		return ligado;
	}

	@Override
	public String toString() {
		return "AreaIrrigationOrderEvent {area=" + getArea() + ", ligado=" + ligado + ", topico=" + getType() + "}";
	}
}
