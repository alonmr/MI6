package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event<List<String>> {
    private List<String> agentsToSend;
    private int time;

    public SendAgentsEvent(List<String> agentsToSend, int duration) {
        this.agentsToSend = agentsToSend;
        time = duration;
    }

    public List<String> getAgents() {
        return agentsToSend;
    }

    public int getTime() {
        return time;
    }

}

