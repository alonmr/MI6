package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;


/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private LinkedList<MissionInfo> missions;
	private Callback<TickBroadcast> callbackTick;

	public Intelligence() {
		super("Intelligence");
	}

	@Override
	protected void initialize() {
		SimplePublisher SP = getSimplePublisher();
		callbackTick = new Callback<TickBroadcast>(){
			@Override
			public void call(TickBroadcast b) {
				if(!missions.isEmpty()) {
					MissionInfo missionInfo = missions.remove();
					SP.sendEvent(new MissionReceivedEvent(missionInfo));
				}
			}
		};
		this.subscribeBroadcast(TickBroadcast.class,callbackTick);
	}

}

