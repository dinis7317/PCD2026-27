package iscot.server;

import iscot.enums.Areas;
import iscot.enums.Type;

public abstract class Client extends Thread {
	private static int clientAmount=-1;
	protected Server server;
	private Areas area;
	public abstract Type getType() ;
	private int id=-1;
	public Areas getArea() {
		return area;
	}
	public int getClientId() {
		return id;
	}

	public Client(Server server, Areas area) {
		super();
		this.server = server;
		this.area = area;
		this.id = ++clientAmount;
	}
	@Override
	public String toString() {
		return "Client [server=" + server + ", area=" + area + ", id=" + id + "]";
	}
}
