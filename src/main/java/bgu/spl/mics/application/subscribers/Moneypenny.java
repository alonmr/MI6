package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.AbstractMap;
import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

    private int id;

    private int currTick;

    public Moneypenny(int id) {
        super("Moneypenny " + id);
        this.id = id;
    }

    @Override
    protected void initialize() {
        Squad ourSquad = Squad.getInstance();
        Callback<TickBroadcast> callbackTimeTickBroadcast = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                currTick = c.getCurrTick();
            }
        };

        Callback<AgentsAvailableEvent> callbackAgentsAvailable = new Callback<AgentsAvailableEvent>() {
            @Override
            public void call(AgentsAvailableEvent e) {
                boolean acquired = ourSquad.getAgents(e.getSerialNumbers());
                if (acquired) {
                    AbstractMap.SimpleEntry<Integer, List<String>> result = new AbstractMap.SimpleEntry<>(id , ourSquad.getAgentsNames(e.getSerialNumbers()));
                    complete(e, result);
                    int send = e.getSend();
                    if (send == 1) {
                        ourSquad.sendAgents(e.getSerialNumbers(), e.getTime());
                    } else if (send == -1) {
                        ourSquad.releaseAgents(e.getSerialNumbers());
                    }
                } else
                    complete(e, new AbstractMap.SimpleEntry<Integer, List<String>>(-1, null));
            }
        };
        this.subscribeEvent(AgentsAvailableEvent.class, callbackAgentsAvailable);
        this.subscribeBroadcast(TickBroadcast.class, callbackTimeTickBroadcast);

    }


}
