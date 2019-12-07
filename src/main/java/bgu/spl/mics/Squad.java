package bgu.spl.mics;
import java.util.*;
public class Squad {
    private static Squad ourInstance = new Squad();

    public static Squad getInstance() {
        return ourInstance;
    }
    private Map<String,Agent> squad;
    private Squad() {
        Map<String,Agent> squad=new TreeMap<String, Agent>();
    }
    public boolean getAgents(List<String> serials){
        //TODO need to acquire before: if an agent is in a mission we wait for him to become available
        ListIterator<String> it = serials.listIterator();
        boolean isInSquadForNow=true;
        while(isInSquadForNow && it.hasNext() ){
            if(!squad.containsKey(it.next())){
                isInSquadForNow=false;
            }
        }
        return isInSquadForNow;
    }

    void releaseAgents(List<String> serials){
        ListIterator<String> it = serials.listIterator();
        while(it.hasNext()){
            squad.get(it).acquire();
            it.next();
        }
    }

    public List<String> getAgentsNames(List<String> serials){
        List<String> agentNames=new LinkedList<String>();
        ListIterator<String> it = serials.listIterator();
        while(it.hasNext()){
            agentNames.add(squad.get(it).getName());
            it.next();
        }
        return  agentNames;
        //TODO void sendAgents(List<String> serial, int timeTick)

        //TODO void load (Agent[] agents)
    }

}
