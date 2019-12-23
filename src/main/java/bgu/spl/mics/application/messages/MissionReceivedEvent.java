package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent<T> implements Event<T> {

    private MissionInfo missionInfo;


    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public String getGadget(){return missionInfo.getGadget();}

    public List<String> getAgents(){return missionInfo.getSerialAgentsNumbers();}

    public int getTimeExpired(){return missionInfo.getTimeExpired();}

}
