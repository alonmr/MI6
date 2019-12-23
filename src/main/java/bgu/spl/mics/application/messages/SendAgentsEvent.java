package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.List;

public class SendAgentsEvent implements Event<Boolean> {
    private List<String> agentsToSend;

    public SendAgentsEvent(List<String> agentsToSend) {
        this.agentsToSend = agentsToSend;
    }

    public List<String> getAgentsToRelease() {
        return agentsToSend;
    }

}

