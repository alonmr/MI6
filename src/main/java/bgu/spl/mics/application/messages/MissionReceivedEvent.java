package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event {
    //TODO: add mission info
    private MissionInfo missionInfo;

    public MissionReceivedEvent(MissionInfo missionInfo) {
        this.missionInfo = missionInfo;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }
}
