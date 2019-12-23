package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.LinkedList;
public class SendAgentsEvent implements Event {
    private LinkedList<String> agentsToSend;

    public SendAgentsEvent(LinkedList<String> agentsToSend) {
        this.agentsToSend = agentsToSend;
    }

    public LinkedList<String> getAgentsToRelease() {
        return agentsToSend;
    }

}

