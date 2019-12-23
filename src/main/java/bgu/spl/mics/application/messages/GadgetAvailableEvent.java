package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<String> {

    private String senderName;
    private String gadget;

    public GadgetAvailableEvent(String senderName, String gadget) {
        this.senderName = senderName;
        this.gadget = gadget;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getGadgets(){return gadget;}

}
