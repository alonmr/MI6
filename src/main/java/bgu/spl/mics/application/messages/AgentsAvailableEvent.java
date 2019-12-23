package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.LinkedList;

public class AgentsAvailableEvent implements Event{

    private LinkedList<String> serialNumbers;

    public AgentsAvailableEvent(LinkedList<String> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public LinkedList<String> getSerialNumbers () {
        return serialNumbers;
    }

}
