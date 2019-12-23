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

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBrokerImpl ourInstance;
	private static HashMap<Subscriber, Queue<Message>> registers;//maybe should be blocking queue
	private static LinkedBlockingQueue<Subscriber> agentsAvailableList;//Subscribers subscribe to this list to get this events
	private static LinkedBlockingQueue<Subscriber> gadgetAvailableList;
	private static LinkedBlockingQueue<Subscriber> sendAgentsList;
	private static LinkedBlockingQueue<Subscriber> releaseAgentsList;
	private static LinkedBlockingQueue<Subscriber> missionAvailableList;//round robin is by event type not by subscriber
	private static List<Subscriber> tickBroadcastList;
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
			tickBroadcastList = new LinkedList<>();
			events = new HashMap<>();
		}
		return ourInstance;
	}

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

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		events.get(e).resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(b.getClass().isAssignableFrom(TickBroadcast.class)) {
			for (Subscriber s : tickBroadcastList) {
				registers.get(s).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {//TODO: split to private methods
		if(e.getClass().isAssignableFrom(MissionReceivedEvent.class)){
			Subscriber s = missionAvailableList.poll();
			registers.get(s).add(e);
			missionAvailableList.remove(s);
			missionAvailableList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(AgentsAvailableEvent.class)){
			Subscriber s = agentsAvailableList.poll();
			registers.get(s).add(e);
			agentsAvailableList.remove(s);
			agentsAvailableList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(GadgetAvailableEvent.class)){
			Subscriber s = gadgetAvailableList.poll();
			registers.get(s).add(e);
			gadgetAvailableList.remove(s);
			gadgetAvailableList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(ReleaseAgentsEvent.class)){
			Subscriber s = releaseAgentsList.poll();
			registers.get(s).add(e);
			releaseAgentsList.remove(s);
			releaseAgentsList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		if(e.getClass().isAssignableFrom(SendAgentsEvent.class)){
			Subscriber s = sendAgentsList.poll();
			registers.get(s).add(e);
			sendAgentsList.remove(s);
			sendAgentsList.add(s);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		registers.put(m,new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {
		registers.remove(m);
		agentsAvailableList.remove(m);
		gadgetAvailableList.remove(m);
		missionAvailableList.remove(m);
		tickBroadcastList.remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		synchronized (registers) {
			while (registers.get(m).isEmpty()) {
				if(Thread.currentThread().isInterrupted())
					throw new InterruptedException();
				wait();
			}
			return registers.get(m).element();
		}
	}

	

}
