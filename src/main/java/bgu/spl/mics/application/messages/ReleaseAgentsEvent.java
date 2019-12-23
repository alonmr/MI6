package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.LinkedList;

public class ReleaseAgentsEvent implements Event {
    private LinkedList<String> agentsToRelease;

    public ReleaseAgentsEvent(LinkedList<String> agentsToRelease) {
        this.agentsToRelease = agentsToRelease;
    }

    public LinkedList<String> getAgentsToRelease() {
        return agentsToRelease;
    }
}

