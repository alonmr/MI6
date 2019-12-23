package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.List;

public class ReleaseAgentsEvent implements Event<Boolean> {
    private List<String> agentsToRelease;

    public ReleaseAgentsEvent(List<String> agentsToRelease) {
        this.agentsToRelease = agentsToRelease;
    }

    public List<String> getAgentsToRelease() {
        return agentsToRelease;
    }
}

