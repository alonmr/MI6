package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;

public class MissionReceivedEvent implements Event<String > {

    private String senderName;
    private MissionInfo info;

    public MissionReceivedEvent(String senderName, MissionInfo missionInfo) {
        this.senderName = senderName;
        info = missionInfo;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getGadget(){return info.getGadget();}

    public List<String> getAgents(){return info.getSerialAgentsNumbers();}

    public int getTimeExpired(){return info.getTimeExpired();}

}
