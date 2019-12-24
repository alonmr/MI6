package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.LinkedList;
import java.util.List;

public class AgentsAvailableEvent implements Event<Integer>{

    private List<String> serialNumbers;


    public AgentsAvailableEvent(List<String> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    public List<String> getSerialNumbers () {
        return serialNumbers;
    }


}
