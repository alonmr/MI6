package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent implements Event<Boolean> {

    private MissionInfo missionInfo;
    private int terminateTime;

    public MissionReceivedEvent(MissionInfo missionInfo,int terminateTime) {
        this.missionInfo = missionInfo;
        this.terminateTime = terminateTime;
    }

    public int getTerminateTime(){return terminateTime;}

    public String getGadget(){return missionInfo.getGadget();}

    public List<String> getAgents(){return missionInfo.getSerialAgentsNumbers();}

    public int getTimeExpired(){return missionInfo.getTimeExpired();}

    public String getMissonName() {return missionInfo.getMissionName();}

    public int getTimeIssued(){return missionInfo.getTimeIssued();}

    public int getDuration(){return missionInfo.getDuration();}



}
