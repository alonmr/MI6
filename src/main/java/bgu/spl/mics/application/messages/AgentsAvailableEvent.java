package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class AgentsAvailableEvent implements Event<Pair<Integer,List<String>>>{

    private List<String> serialNumbers;
    private int time;
    private int send;

    public AgentsAvailableEvent(List<String> serialNumbers,int duration) {
        this.serialNumbers = serialNumbers;
        time=duration;
    }

    public List<String> getSerialNumbers () {
        return serialNumbers;
    }

    public int getTime(){return time;}

    public synchronized void setSend(int send){this.send = send;notifyAll();}

    public int getSend() {
        synchronized (this) {
            while (send == 0) try{wait();} catch (Exception ignored){}
            return send;
        }
    }
}
