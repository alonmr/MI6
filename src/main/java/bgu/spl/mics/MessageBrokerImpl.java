package bgu.spl.mics;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static MessageBrokerImpl ourInstance;
	private static ConcurrentHashMap<String, Queue<Message>> registers;//maybe should be blocking queue
	private static ConcurrentHashMap<Class<? extends Broadcast>,LinkedBlockingQueue<Subscriber>> broadcastSubscribers;
	private static ConcurrentHashMap<Class<? extends Event>,LinkedBlockingQueue<Subscriber>> messagesSubscribers;
	private static ConcurrentHashMap<Event,Future> events;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		if (ourInstance == null) {
			ourInstance = new MessageBrokerImpl();
			registers = new ConcurrentHashMap<>();
			messagesSubscribers = new ConcurrentHashMap<>();
			broadcastSubscribers= new ConcurrentHashMap<>();
			events = new ConcurrentHashMap<>();
		}
		return ourInstance;
	} //TODO: make msgBroker Safe Singelton

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (messagesSubscribers) {
			if (!messagesSubscribers.containsKey(type))
				messagesSubscribers.put(type, new LinkedBlockingQueue<>());
			messagesSubscribers.get(type).add(m);
		}
	}


	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (broadcastSubscribers) {
			if (!broadcastSubscribers.containsKey(type))
				broadcastSubscribers.put(type, new LinkedBlockingQueue<>());
			broadcastSubscribers.get(type).add(m);
			//System.out.println(m.getName()+" subscribed to "+type);
		}
	}


	@Override
	public <T> void complete(Event<T> e, T result) {
		events.get(e).resolve(result);

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (broadcastSubscribers){
		if (!broadcastSubscribers.get(b.getClass()).isEmpty()) {
			for (Subscriber s : broadcastSubscribers.get(b.getClass())) {
				registers.get(s.getName()).add(b);
			}
		}
	}
}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (messagesSubscribers) {
			if (!messagesSubscribers.get(e.getClass()).isEmpty()) {
				Subscriber s = messagesSubscribers.get(e.getClass()).poll();
				System.out.println(s.getName() + " adding " + e.toString());
				registers.get(s.getName()).add(e);
				messagesSubscribers.get(e.getClass()).add(s);
				Future<T> future = new Future<>();
				events.put(e, future);
				return future;
			}
			return null;
		}
	}

	@Override
	public void register(Subscriber m) {
		registers.put(m.getName(),new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(Subscriber m) {
		System.out.println(m.getName()+" unregisters");
		messagesSubscribers.forEach((key,value)-> value.remove(m));
		broadcastSubscribers.forEach((key,value)->value.remove(m));
		registers.remove(m.getName());
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		while (registers.get(m.getName()).isEmpty()) {
			//System.out.println(m.getName()+"waiting");
			if(Thread.currentThread().isInterrupted())
				throw new InterruptedException();
			sleep(100);
		}
		return registers.get(m.getName()).poll();
	}
}



