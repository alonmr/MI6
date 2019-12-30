package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event<Integer> {

    private String gadget;
    /**
     * @param gadget the gadget the mission needs to execute
     */
    public GadgetAvailableEvent(String gadget) {
        this.gadget = gadget;
    }
    /**
     * Retrieves the gadget the mission needs to execute.
     */
    public String getGadget() {
        return gadget;
    }

}
