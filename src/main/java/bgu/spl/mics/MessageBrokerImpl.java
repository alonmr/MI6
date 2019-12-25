package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBrokerImpl ourInstance;
	private static HashMap<String, Queue<Message>> registers;//maybe should be blocking queue
	private static LinkedBlockingQueue<Subscriber> agentsAvailableList;//Subscribers subscribe to this list to get this events
	private static LinkedBlockingQueue<Subscriber> gadgetAvailableList;
	private static LinkedBlockingQueue<Subscriber> sendAgentsList;
	private static LinkedBlockingQueue<Subscriber> releaseAgentsList;
	private static LinkedBlockingQueue<Subscriber> missionAvailableList;
	private static LinkedBlockingQueue<Subscriber> tickBroadcastList;
	private static LinkedBlockingQueue<Subscriber> terminateBroadcastList;
	private static HashMap<Event,Future> events;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if (ourInstance == null) {
			ourInstance = new MessageBrokerImpl();
			registers = new HashMap<>();
			agentsAvailableList = new LinkedBlockingQueue<>();
			gadgetAvailableList = new LinkedBlockingQueue<>();
			sendAgentsList = new LinkedBlockingQueue<>();
			releaseAgentsList = new LinkedBlockingQueue<>();
			missionAvailableList = new LinkedBlockingQueue<>();
			tickBroadcastList = new LinkedBlockingQueue<>();
			terminateBroadcastList = new LinkedBlockingQueue<>();
			events = new HashMap<>();
		}
		return ourInstance;
	} //TODO: make msgBroker Safe Singelton

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if(type.isAssignableFrom(AgentsAvailableEvent.class))
			agentsAvailableList.add(m);
		if(type.isAssignableFrom(GadgetAvailableEvent.class))
			gadgetAvailableList.add(m);
		if(type.isAssignableFrom(MissionReceivedEvent.class))
			missionAvailableList.add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(type.isAssignableFrom(TickBroadcast.class))
			tickBroadcastList.add(m);
		if(type.isAssignableFrom(TerminateBroadcast.class))
			terminateBroadcastList.add(m);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		events.get(e).resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(b.getClass().isAssignableFrom(TickBroadcast.class) & !tickBroadcastList.isEmpty()) {
			synchronized (tickBroadcastList){
			for (Subscriber s : tickBroadcastList) {
				System.out.println(s.getName() + " adding broadcast to queue");
				registers.get(s.getName()).add(b);
			}
			}
		}
		if(b.getClass().isAssignableFrom(TerminateBroadcast.class) & !terminateBroadcastList.isEmpty()) {
			for (Subscriber s : terminateBroadcastList) {
				System.out.println(s.getName()+" adding terminate broadcast to queue");
				registers.get(s.getName()).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {//TODO: split to private methods
		//TODO: messageBroker shouldn't know what kind of messages it receives.
		if(e.getClass().isAssignableFrom(MissionReceivedEvent.class) & !missionAvailableList.isEmpty()){
			Subscriber s = missionAvailableList.poll();
			System.out.println(s.getName()+" adding mission received");
			registers.get(s.getName()).add(e);
			missionAvailableList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(AgentsAvailableEvent.class) & !agentsAvailableList.isEmpty()){ ;
			Subscriber s = agentsAvailableList.poll();
			System.out.println(agentsAvailableList.toString());
			System.out.println(s.getName()+" adding agents available");
			registers.get(s.getName()).add(e);
			agentsAvailableList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(GadgetAvailableEvent.class) & !gadgetAvailableList.isEmpty()){
			Subscriber s = gadgetAvailableList.poll();
			registers.get(s.getName()).add(e);
			gadgetAvailableList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(ReleaseAgentsEvent.class) & !releaseAgentsList.isEmpty()){
			Subscriber s = releaseAgentsList.poll();
			registers.get(s.getName()).add(e);
			releaseAgentsList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(SendAgentsEvent.class) & !sendAgentsList.isEmpty()){
			Subscriber s = sendAgentsList.poll();
			registers.get(s.getName()).add(e);
			sendAgentsList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		return null;
	}

	@Override//TODO: change to normal queue
	public void register(Subscriber m) {
		registers.put(m.getName(),new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {
		System.out.println(m.getName()+" unregisters");
		agentsAvailableList.remove(m);
		gadgetAvailableList.remove(m);
		missionAvailableList.remove(m);
		tickBroadcastList.remove(m);
		terminateBroadcastList.remove(m);
		registers.remove(m.getName());
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		synchronized (registers) {
			while (registers.get(m.getName()).isEmpty()) {
				System.out.println(m.getName()+"waiting");
				if(Thread.currentThread().isInterrupted())
					throw new InterruptedException();
				sleep(1000);
			}
			return registers.get(m.getName()).poll();
		}
	}


}
