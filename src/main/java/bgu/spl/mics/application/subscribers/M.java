package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private int currTick;
    private int id;

    public M(int id) {
        super("M " + id);
        this.id = id;
        currTick = 0;
    }

    @Override
    protected void initialize() {
        Callback<TickBroadcast> callbackTimeTickBroadcast = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                currTick = c.getCurrTick();
            }
        };
        Callback<MissionReceivedEvent> callbackMissionReceived = new Callback<MissionReceivedEvent>() {
            @Override
            public void call(MissionReceivedEvent e) {
                Diary.getInstance().incrementTotal();
                SimplePublisher SP = getSimplePublisher();
                boolean complete = false;
                AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(e.getAgents(), e.getDuration());
                Future<AbstractMap.SimpleEntry<Integer, List<String>>> hasAgents = SP.sendEvent(agentsAvailableEvent);
                AbstractMap.SimpleEntry<Integer, List<String>> agents =null;
                if(hasAgents != null) {
                    int slept = 1;
                    while (slept <= (e.getTimeExpired()) - e.getTimeIssued() && slept <= (e.getTerminateTime() - e.getTimeIssued())) {
                        agents = hasAgents.get(100, TimeUnit.MILLISECONDS);
                        slept++;
                    }
                    if (agents != null && agents.getKey() != -1) {
                        Future<Integer> hasGadget = SP.sendEvent(new GadgetAvailableEvent(e.getGadget()));
                        if (hasGadget != null && hasGadget.get() != -1 && currTick < e.getTimeExpired()) {
                            executeMission(agents,agentsAvailableEvent,e,hasGadget);
                            complete = true;
                        } else {
                            complete(e, false);
                            agentsAvailableEvent.setSend(-1);
                        }
                    }
                }
                if (!complete) {
                    agentsAvailableEvent.setSend(-1);
                }
            }

            private void executeMission(AbstractMap.SimpleEntry<Integer, List<String>> agents,AgentsAvailableEvent agentsAvailableEvent,MissionReceivedEvent e,Future<Integer> hasGadget){
                List<String> agentsNames = agents.getValue();
                int mpid = agents.getKey();//access to moneypennyid
                agentsAvailableEvent.setSend(1);
                complete(e, true);
                Diary.getInstance().addReport(new Report(e.getMissionName(), id, mpid, e.getAgents(),
                        agentsNames, e.getGadget(), e.getTimeIssued(), hasGadget.get(), currTick));
            }
        };
        this.subscribeEvent(MissionReceivedEvent.class, callbackMissionReceived);
        this.subscribeBroadcast(TickBroadcast.class, callbackTimeTickBroadcast);
    }

}
