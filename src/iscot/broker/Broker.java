package iscot.broker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import iscot.area.Area;
import iscot.enums.Areas;
import iscot.enums.Type;
import iscot.event.Event;
import iscot.server.Server;

public class Broker{
	private Map<Type,List<Event>> messages =new HashMap<Type, List<Event>>();
	private Map<Event,CountDownLatch> cdls=new HashMap<Event,CountDownLatch>();
	private Map<Event,List<Integer>> clientsAlreadyServed=new HashMap<Event,List<Integer>>();
	private Server server;
	public Broker(Server server) {
		this.server = server;
		for(Type type:Type.values())
			messages.put(type, new ArrayList<Event>());
	}

	public synchronized void queueMessage(Event message) {
		messages.get(message.getType()).add(message);
		CountDownLatch cdl=new CountDownLatch(server.countClientsFor(message.getType(), message.area));
		List<Integer> clientList=new ArrayList<>();
		cdls.put(message, cdl);
		clientsAlreadyServed.put(message, clientList);
		System.err.println("Queued "+message+" numMessages:"+cdls.size());
		new Thread(new Runnable() {
			@Override
			public void run() {				
				try {
					System.err.println("Will await for removal of "+message+" #"+cdl.getCount());
					cdl.await();
					System.err.println("Triggering message removal:"+message);
					removeMessage(message);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		notifyAll();
//		System.err.println("Broker: message queued!");
	}
	
	protected synchronized void removeMessage(Event message) {
		messages.get(message.getType()).remove(message);
		cdls.remove(message);
		clientsAlreadyServed.remove(message);
		server.notifyDeletedMessage(message);
		System.err.println("Removing "+message+" #messages:"+cdls.size());
		notifyAll();
	}

	public synchronized Event getMessage(Type type, Areas area,int clientNumber) throws InterruptedException {
		while(messages.isEmpty() || noMessageFor(type,area) || clientsAlreadyServed.get(messages.get(type).get(0)).contains(clientNumber)) {
//			System.err.println(" Wait in getMessage!:"+messages+ "Asked for:"+type+area);
			wait();
		}
		// register that this client has already received the message
		clientsAlreadyServed.get(messages.get(type).get(0)).add(clientNumber);
		cdls.get(messages.get(type).get(0)).countDown();
		return messages.get(type).get(0);
	}

	private boolean noMessageFor(Type type, Areas area) {
//		System.err.println("Checking messages for:"+area+" Type:"+type);
		return messages.get(type).isEmpty() ||  messages.get(type).get(0).area!=area ;
	}

	// Just returns the queue, without any consequence in queue state, no remocals
	public List<Event> checkMessageQueue() {
		List<Event> returnedMessages=new ArrayList<>();
		Collection<List<Event>> allLists=messages.values();
//		for(List<Message> list:allLists)
		returnedMessages.addAll(returnedMessages);
		return returnedMessages;
	}
}
