package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class AgentsAvailableEvent implements Event<String> {

    private String senderName;

    public AgentsAvailableEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
