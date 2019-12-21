package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private SimplePublisher m;
	private int id;
	private Callback<MissionReceivedEvent> callbackName;
	public M(int id) {
		super("M");
		this.id=id;
	}

	@Override
	protected void initialize() {
		MessageBroker messageBrokerTest = MessageBrokerImpl.getInstance();
		messageBrokerTest.register(this);
		messageBrokerTest.subscribeEvent(MissionReceivedEvent.class,this);

		// anonymous class, the method ‘call’ is overriden
		callbackName = new Callback<MissionReceivedEvent>(){
			@Override
			public void call(MissionReceivedEvent e) {
				Diary.getInstance().incrementTotal();

			}
		};

		this.subscribeEvent(MissionReceivedEvent.class, callbackName);

	}

}
