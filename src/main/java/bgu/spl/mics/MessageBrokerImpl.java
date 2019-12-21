package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
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
	private static HashMap<Subscriber, LinkedBlockingQueue<Message>> registers;//maybe should be blocking queue
	private static List<Subscriber> agentsAvailableList;//Subscribers subscribe to this list to get this events
	private static List<Subscriber> gadgetAvailableList;
	private static List<Subscriber> missionAvailableList;
	private static List<Subscriber> tickBroadcastList;
	private static List<M> MsRoundRobin;
	private static List<Moneypenny> MoneysRoundRobin;
	private static HashMap<Event,Future> events;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if (ourInstance == null) {
			ourInstance = new MessageBrokerImpl();
			registers = new HashMap<>();
			agentsAvailableList = new LinkedList<>();
			gadgetAvailableList = new LinkedList<>();
			missionAvailableList = new LinkedList<>();
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
	public <T> Future<T> sendEvent(Event<T> e) {//TODO: imp for other events
		if(e.getClass().isAssignableFrom(MissionReceivedEvent.class)){
			registers.get(missionAvailableList.get(0)).add(e);
			Future<T> future = new Future<>();
			events.put(e,future);
			return future;
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		registers.put(m,new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(Subscriber m) {
		registers.remove(m);
		agentsAvailableList.remove(m);
		gadgetAvailableList.remove(m);
		missionAvailableList.remove(m);
		tickBroadcastList.remove(m);
		MsRoundRobin.remove(m);
		MoneysRoundRobin.remove(m);
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
