package iscot.area;

import java.util.ArrayList;
import java.util.List;

import iscot.enums.Areas;
import iscot.server.Client;
import iscot.server.DealWithClient;

public class Area {
	public final Areas name;
	private List<Client> clients= new ArrayList<>();
	public Area(Areas name) {
		super();
		this.name = name;
	}
	public List<Client> getClients() {
		return clients;
	}
	
}
