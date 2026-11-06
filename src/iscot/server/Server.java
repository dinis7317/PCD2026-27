package iscot.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import iscot.area.Area;
import iscot.broker.Broker;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.event.sensor.LightSensorEvent;
import iscot.monitor.SystemMonitor;

@SuppressWarnings("deprecation")
public class Server extends Observable{
	private Broker broker= new Broker(this);
	private ServerSocket serverSocket;
	private Map<Areas,Area> areas= new HashMap<>();

	public Server(int portNumber, List<Area> areas) throws IOException {
		serverSocket= new ServerSocket(portNumber);
		for(Area area:areas)
			this.areas.put(area.name, area);
	}

	/* Server that manages the broker.
	 * @params
	 *  - int portNumber: for connection requests
	 */
	public static void main(String[] args) throws IOException {
		if(args.length !=1) {
			System.err.println("Invalid arguments!");
		}
		else {
			int portNumber= Integer.parseInt(args[0]);
			List<Area> areas=new ArrayList<Area>(Areas.values().length);
			for(int i=1; i!=Areas.values().length;i++)
				areas.add(new Area(Areas.values()[i]));
			System.err.println("Existing areas:"+areas.size());
			Server server= new Server(portNumber,areas);
			new SystemMonitor(server);
			server.startServing();	

		}
	}

	private void startServing() {
		while(true)
			try {
				Socket socket=serverSocket.accept();
				new DealWithClient(this, null,new ObjectInputStream(socket.getInputStream()),new ObjectOutputStream(socket.getOutputStream()) ).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Connection failed!");
				e.printStackTrace();
			}
	}

	public void register(Client client) {
		if(!areaExists(client.getArea()) )
			throw new IllegalArgumentException("Invalid Area:"+client.getArea());
		if(isAlreadyRegistered(client))
			throw new IllegalArgumentException("Sensor already registered!");

		areas.get(client.getArea()).getClients().add(client);

		// Debug
		setChanged();
		notifyObservers(client);

	}

	private boolean areaExists(Areas areas2) {
		return areas.containsKey(areas2);
	}

	private boolean isAlreadyRegistered(Client client2) {
		if(areas.get(client2.getArea())==null || areas.get(client2.getArea()).getClients().isEmpty())
			return false;
		for(Client client:areas.get(client2.getArea()).getClients())
			if(client.getClientId()==client2.getClientId())
				return true;
		return false;
	}

	public void queueMessage(Event message) {
		broker.queueMessage(message);

		// Debug
		setChanged();
		notifyObservers(message);

	}

	public Event getMessage(Type type, Areas areas2, int clientNumber) throws InterruptedException {
		return 	broker.getMessage(type, areas2,clientNumber); // single message in requested area
	}
	public List<Event> checkMessageQueue(){
		return broker.checkMessageQueue();
	}
	public int countClientsFor(Type type, Areas area) {
		int count=0;
		for(Client client:areas.get(area).getClients())
			if(client.getType()==Type.DECIDER && type==Type.SENSOR)
				count++;
			else if(client.getType()==Type.ACTUATOR && type==Type.DECIDER)
				count++;
		return count;
	}

	public void notifyDeletedMessage(Event message) {
		// Debug
		setChanged();
		notifyObservers(message);		
	}

}
