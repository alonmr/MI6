package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class MissionReceivedEvent implements Event<String > {
    //TODO: add mission info
    private String senderName;

    public MissionReceivedEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
