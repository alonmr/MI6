package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    private int currTick;
    /**
     * @param currTick the time tick to broadcast
     */
    public TickBroadcast(int currTick) {
        this.currTick = currTick;
    }
    /**
     * Retrieves the current time tick.
     * <p>
     *
     * @return the current time tick.
     */
    public int getCurrTick() {
        return currTick;
    }
}
