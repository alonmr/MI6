package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class MissionReceivedEvent implements Event<String > {

    private String senderName;

    public MissionReceivedEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
