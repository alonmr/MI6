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
import javafx.util.Pair;

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
                System.out.println(getName() + " " + currTick);
            }
        };
        Callback<MissionReceivedEvent> callbackMissionReceived = new Callback<MissionReceivedEvent>() {
            @Override
            public void call(MissionReceivedEvent e) {
                Diary.getInstance().incrementTotal();
                System.out.println(Diary.getInstance().getTotal() + ":total");
                SimplePublisher SP = getSimplePublisher();
                boolean complete = false;
                AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(e.getAgents(), e.getDuration());
                Pair<Integer, List<String>> agents =
                        SP.sendEvent(agentsAvailableEvent).get((Math.min(e.getTerminateTime(), e.getTimeExpired()) - e.getTimeIssued()) * 100, TimeUnit.MILLISECONDS);
                if (agents != null && agents.getKey() != -1) {
                    Future<Integer> hasGadget = SP.sendEvent(new GadgetAvailableEvent(e.getGadget()));
                    if (hasGadget != null && hasGadget.get() != -1 && currTick < e.getTimeExpired()) {
                        List<String> agentsNames = agents.getValue();
                        int mpid = agents.getKey();//access to moneypennyid
                        agentsAvailableEvent.setSend(1);
                        System.out.println("moneypenny " + mpid + "should send agents");
                        //Future<List<String>> sendAgents = SP.sendEvent(new SendAgentsEvent(e.getAgents(),e.getDuration()));
                        complete(e, true);
                        complete = true;
                        System.out.println("completed mission " + getName() + " " + e.getMissonName());
                        Diary.getInstance().addReport(new Report(e.getMissonName(), id, mpid, e.getAgents(),
                                agentsNames, e.getGadget(), e.getTimeIssued(), hasGadget.get(), currTick));
                    } else {
                        complete(e, false);
                        agentsAvailableEvent.setSend(-1);
                        System.out.println("moneypenny " + agents.getKey() + "should release agents");
                        System.out.println("failed mission no gadget or " + currTick + ">" + e.getTimeExpired());
                    }

                }
                if (!complete) {
                    //Future<Boolean> releaseAgents = SP.sendEvent(new ReleaseAgentsEvent(e.getAgents()));
                    //releaseAgents.get();
                    agentsAvailableEvent.setSend(-1);
                    System.out.println("failed mission " + e.getMissonName() + " no agents " + getName());
                }
            }

        };

        this.

                subscribeEvent(MissionReceivedEvent.class, callbackMissionReceived);
        this.

                subscribeBroadcast(TickBroadcast.class, callbackTimeTickBroadcast);
    }

}
