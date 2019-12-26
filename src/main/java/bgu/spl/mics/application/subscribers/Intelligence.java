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
	private int id;

	public Intelligence(LinkedList<MissionInfo> missions,int id) {
		super("Intelligence "+id);
		this.missions=missions;
	}

	@Override
	protected void initialize() {
		SimplePublisher SP = getSimplePublisher();
		callbackTick = new Callback<TickBroadcast>(){
			@Override
			public void call(TickBroadcast b) {
				if(!missions.isEmpty()) {
					int timeIssued = missions.element().getTimeIssued();
					if (timeIssued <= b.getCurrTick()) {
						MissionInfo missionInfo = missions.remove();
						SP.sendEvent(new MissionReceivedEvent(missionInfo));
					}
				}
			}
		};
		this.subscribeBroadcast(TickBroadcast.class,callbackTick);
	}

}

