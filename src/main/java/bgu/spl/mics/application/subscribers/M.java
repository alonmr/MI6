package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int currTick;
	private int id;

	public M(int id) {
		super("M "+id);
		this.id=id;
		currTick=0;
	}

	@Override
	protected void initialize() {
		Callback<TickBroadcast> callbackTimeTickBroadcast = new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast c) {
				currTick = c.getCurrTick();
				System.out.println(getName()+" "+currTick);
			}
		};
		Callback<MissionReceivedEvent> callbackMissionReceived = new Callback<MissionReceivedEvent>() {
			@Override
			public void call(MissionReceivedEvent e) {
				Diary.getInstance().incrementTotal();
				SimplePublisher SP = getSimplePublisher();
				boolean complete = false;
				Future<Integer> hasAgents = SP.sendEvent(new AgentsAvailableEvent(e.getAgents()));
				if (hasAgents.get((e.getTimeExpired()-e.getTimeIssued())*100,TimeUnit.NANOSECONDS) != null) {
					Future<Integer> hasGadget = SP.sendEvent(new GadgetAvailableEvent(e.getGadget()));
					if (hasGadget.get() != -1 && currTick < e.getTimeExpired()) {
						Future<List<String>> sendAgents = SP.sendEvent(new SendAgentsEvent(e.getAgents(),e.getDuration()));
						complete(e, true);
						complete = true;
						System.out.println("completed mission");
						Diary.getInstance().addReport(new Report(e.getMissonName(), id, hasAgents.get(), e.getAgents(),
									sendAgents.get(), e.getGadget(), e.getTimeIssued(), hasGadget.get(), currTick));
					} else {
						complete(e, false);
						System.out.println("failed mission no gadget");
					}
				}
				if (!complete) {
					Future<Boolean> releaseAgents = SP.sendEvent(new ReleaseAgentsEvent(e.getAgents()));
					releaseAgents.get();
					System.out.println("failed mission no agents");
				}
			}
		};

		this.subscribeEvent(MissionReceivedEvent.class, callbackMissionReceived);
		this.subscribeBroadcast(TickBroadcast.class, callbackTimeTickBroadcast);
	}

}
