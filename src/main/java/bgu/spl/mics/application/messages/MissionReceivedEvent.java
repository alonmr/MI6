package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent implements Event<Boolean> {

    private MissionInfo missionInfo;
    private int terminateTime;
    /**
     * @param missionInfo the Missioninfo of the mission received.
     * @param terminateTime the termination time of the program.
     */
    public MissionReceivedEvent(MissionInfo missionInfo, int terminateTime) {
        this.missionInfo = missionInfo;
        this.terminateTime = terminateTime;
    }
    /**
     * Retrieves the termination time tof the program.
     */
    public int getTerminateTime() {
        return terminateTime;
    }
    /**
     * Retrieves the gadget name.
     */
    public String getGadget() {
        return missionInfo.getGadget();
    }
    /**
     * Retrieves the serial agent number.
     */
    public List<String> getAgents() {
        return missionInfo.getSerialAgentsNumbers();
    }
    /**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
    public int getTimeExpired() {
        return missionInfo.getTimeExpired();
    }
    /**
     * Retrieves the name of the mission.
     */
    public String getMissionName() {
        return missionInfo.getMissionName();
    }
    /**
     * Retrieves the time the mission was issued in milliseconds.
     */
    public int getTimeIssued() {
        return missionInfo.getTimeIssued();
    }
    /**
     * Retrieves the duration of the mission in time-ticks.
     */
    public int getDuration() {
        return missionInfo.getDuration();
    }


}
