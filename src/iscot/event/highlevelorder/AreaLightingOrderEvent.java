package iscot.event.highlevelorder;

import iscot.enums.Areas;

public class AreaLightingOrderEvent extends HighLevelOrderEvent {

	private final boolean ligado;

	public AreaLightingOrderEvent(Areas area, boolean ligado) {
		super(area);
		this.ligado = ligado;
	}

	public boolean isLigado() {
		return ligado;
	}

	@Override
	public String toString() {
		return "AreaLightingOrderEvent {area=" + getArea() + ", ligado=" + ligado + ", topico=" + getType() + "}";
	}
}
