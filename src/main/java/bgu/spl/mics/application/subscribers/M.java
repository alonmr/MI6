package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

	public M() {
		super("Change_This_Name");
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		MessageBroker messageBrokerTest = MessageBrokerImpl.getInstance();
		messageBrokerTest.register(this);
		messageBrokerTest.subscribeEvent(MissionReceivedEvent.class,this);
	}

}
