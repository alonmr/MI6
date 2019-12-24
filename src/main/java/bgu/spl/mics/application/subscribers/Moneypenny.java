package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private int id;

	private int currTick;
	public Moneypenny(int id) {
		super("Moneypenny");
		this.id = id;
	}

	@Override
	protected void initialize() {
		MessageBroker messageBroker = MessageBrokerImpl.getInstance();
		messageBroker.register(this);
		messageBroker.subscribeEvent(AgentsAvailableEvent.class,this);
		messageBroker.subscribeEvent(SendAgentsEvent.class,this);
		messageBroker.subscribeEvent(ReleaseAgentsEvent.class,this);

		Callback<TickBroadcast> callbackTimeTickBroadcast = new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast c) {
				currTick = c.getCurrTick();
			}
		};

		Callback<AgentsAvailableEvent> callbackAgentsAvailable = new Callback<AgentsAvailableEvent>() {
			@Override
			public void call(AgentsAvailableEvent e) {

			}
		};

		Callback<SendAgentsEvent> callbackSendAgents = new Callback<SendAgentsEvent>() {
			@Override
			public void call(SendAgentsEvent e) {

			}
		};

		Callback<ReleaseAgentsEvent> callbackReleaseAgents = new Callback<ReleaseAgentsEvent>() {
			@Override
			public void call(ReleaseAgentsEvent e) {

			}
		};

		this.subscribeEvent(AgentsAvailableEvent.class, callbackAgentsAvailable);

	}


}
