package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int currTick;
	private int id;
	private Callback<TickBroadcast> callbackTimeTickBroadcast;
	private Callback<MissionReceivedEvent> callbackMissionReceived;
	public M(int id) {
		super("M");
		this.id=id;
		currTick=0;
	}

	@Override
	protected void initialize() {
		MessageBroker messageBroker = MessageBrokerImpl.getInstance();
		messageBroker.register(this);
		messageBroker.subscribeEvent( MissionReceivedEvent.class,this);
		messageBroker.subscribeBroadcast(TickBroadcast.class,this);
		callbackTimeTickBroadcast=new Callback<TickBroadcast>(){
			@Override
			public void call(TickBroadcast c) {
				currTick=c.getCurrTick();
			}
		};
		callbackMissionReceived = new Callback<MissionReceivedEvent>(){
			@Override
			public void call(MissionReceivedEvent e) {
				Diary.getInstance().incrementTotal();
				if(currTick<e.getTimeExpired()) {
					Future<Boolean> hasAgents = messageBroker.sendEvent(new AgentsAvailableEvent(e.getAgents()));
					if (hasAgents.get()) {
						Future<Boolean> hasGadget = messageBroker.sendEvent(new GadgetAvailableEvent(e.getGadget()));
						if (hasGadget.get()) {

							messageBroker.complete(e,true);

						}
						else{
							messageBroker.complete(e,false);
							Future<Boolean> releaseAgents= messageBroker.sendEvent(new ReleaseAgentsEvent(e.getAgents()));
						}
						Future<Boolean>
					}
				}
			}
		};

		this.subscribeEvent(MissionReceivedEvent.class, callbackMissionReceived);

	}

}
