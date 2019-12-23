package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.ReleaseAgentsEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private int id;
	private Callback<AgentsAvailableEvent> callbackAgentsAvailable;
	private Callback<SendAgentsEvent> callbackSendAgents;
	private Callback<ReleaseAgentsEvent> callbackReleaseAgents;

	public Moneypenny(int id) {
		super("Moneypenny");
		this.id = id;
	}

	@Override
	protected void initialize() {
		MessageBroker messageBrokerTest = MessageBrokerImpl.getInstance();
		messageBrokerTest.register(this);
		messageBrokerTest.subscribeEvent(AgentsAvailableEvent.class,this);

		// anonymous class, the method ‘call’ is overriden
		callbackAgentsAvailable = new Callback<AgentsAvailableEvent>(){
			@Override
			public void call(AgentsAvailableEvent e) {

			}
		};

		callbackSendAgents = new Callback<SendAgentsEvent>(){
			@Override
			public void call(SendAgentsEvent e) {

			}
		};

		callbackReleaseAgents = new Callback<ReleaseAgentsEvent>(){
			@Override
			public void call(ReleaseAgentsEvent e) {

			}
		};

		this.subscribeEvent(AgentsAvailableEvent.class, callbackAgentsAvailable);

	}


}
