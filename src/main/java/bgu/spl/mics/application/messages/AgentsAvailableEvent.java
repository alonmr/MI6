package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.AbstractMap;
import java.util.List;

public class AgentsAvailableEvent implements Event<AbstractMap.SimpleEntry<Integer, List<String>>> {

    private List<String> serialNumbers;
    private int time;
    private int send;

    /**
     * @param serialNumbers the serials of the agents needed to execute the mission
     * @param duration the time the mission should take
     */
    public AgentsAvailableEvent(List<String> serialNumbers, int duration) {
        this.serialNumbers = serialNumbers;
        time = duration;
    }
    /**
     * Retrieves the serial numbers of the agents for the mission.
     * <p>
     *
     * @return the list of the serial numbers.
     */
    public List<String> getSerialNumbers() {
        return serialNumbers;
    }
    /**
     * Retrieves the time the mission should take.
     * <p>
     *
     * @return the time.
     */
    public int getTime() {
        return time;
    }

    /**
     * Resolves the result of send object.
     * @param send 1 means to execute and -1 means to abort
     */
    public synchronized void setSend(int send) {
        this.send = send;
        notifyAll();
    }

    /**
     * retrieves the result of send.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
     * @return return the result of type int if it is not 0, if not wait until it is available.
     */
    public int getSend() {
        synchronized (this) {
            while (send == 0) try {
                wait();
            } catch (Exception ignored) {
            }
            return send;
        }
    }
}
