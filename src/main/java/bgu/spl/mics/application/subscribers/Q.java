package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	Callback<GadgetAvailableEvent> callbackGadgetAvailable;

	public Q() {
		super("Change_This_Name");
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		MessageBroker messageBrokerTest = MessageBrokerImpl.getInstance();
		messageBrokerTest.register(this);
		messageBrokerTest.subscribeEvent(GadgetAvailableEvent.class,this);

		// anonymous class, the method ‘call’ is overriden
		callbackGadgetAvailable = new Callback<GadgetAvailableEvent>(){
			@Override
			public void call(GadgetAvailableEvent e) {

			}
		};

		this.subscribeEvent(GadgetAvailableEvent.class, callbackGadgetAvailable);

	}

}
