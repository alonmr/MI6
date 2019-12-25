package bgu.spl.mics.application;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        MessageBrokerImpl.getInstance();
        Inventory inventoryInstance = Inventory.getInstance();
        Squad squadInstance = Squad.getInstance();


        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader("input201.json"));
            loadToInventory(obj,inventoryInstance);
            loadToSquad(obj,squadInstance);
            List<Intelligence> intelligenceList=new LinkedList<Intelligence>();
            addToIntelligenceList(obj,intelligenceList);
            JSONObject servicesObject=getServicesObject(obj);



            TimeService timeService=new TimeService(Integer.parseInt(servicesObject.get("time").toString()));
            Thread TS = new Thread(timeService);
            TS.start();
            ExecutorService intelligence = Executors.newFixedThreadPool(intelligenceList.size());
            for(int i = 0; i<intelligenceList.size() ; i++){
                intelligence.execute(intelligenceList.get(i));
            }
            intelligence.shutdown();
            int amountOfM=Integer.parseInt(servicesObject.get("M").toString());
            int amountOfMoneyPenny=Integer.parseInt(servicesObject.get("Moneypenny").toString());
            ExecutorService m = Executors.newFixedThreadPool(amountOfM);
            for(int i = 0; i<amountOfM ; i++){
                m.execute(new M(i+1));
            }
            m.shutdown();
            ExecutorService moneyPenny = Executors.newFixedThreadPool(amountOfMoneyPenny-1);
            for(int i = 1; i<amountOfMoneyPenny ; i++){
                moneyPenny.execute(new Moneypenny(i+1));
            }
            moneyPenny.shutdown();
            Thread moneyPennyForRelease = new Thread(new Moneypenny(1));
            moneyPennyForRelease.start();
            Thread Q =new Thread(new Q());
            Q.start();
            TS.join();//time service is done
            //terminate all
            System.out.println("terminated");
            Inventory.getInstance().printToFile("inventoryOutputFile");
            Diary.getInstance().printToFile("diaryOutputFile");
        } catch (ParseException | IOException | InterruptedException ex) {
            System.out.println("exception caught");
        }
    }

    private static JSONObject getServicesObject(Object obj) {
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject servicesObject= (JSONObject) jsonObject.get("services");
        return servicesObject;
    }

    private static void addToIntelligenceList(Object obj, List<Intelligence> intelligenceList) {
        JSONObject servicesObject= getServicesObject(obj);
        JSONArray intelligenceArray=(JSONArray) servicesObject.get("intelligence");
        for(int i=0;i<intelligenceArray.size();i++) {
            LinkedList<MissionInfo> missionInfoList=new LinkedList<MissionInfo>();
            JSONObject missionArray = (JSONObject) intelligenceArray.get(i);
            JSONArray insideMission = (JSONArray) missionArray.get("missions");
            insideMission.forEach(data -> parseMissionArray((JSONObject) data,missionInfoList));
            Intelligence intelligence= new Intelligence(missionInfoList,i+1);
            intelligenceList.add(intelligence);
        }
    }

    private static void loadToSquad(Object obj, Squad squadInstance) {
        JSONObject jsonObject = (JSONObject) obj;
        HashMap<String, Agent> agents = new HashMap<String, Agent>();
        JSONArray squadJsonArray = (JSONArray) jsonObject.get("squad");
        squadJsonArray.forEach( squad -> parseSquadObject( (JSONObject) squad ,agents ));
        squadInstance.load(agents);

    }

    private static void loadToInventory(Object obj, Inventory inventoryInstance) {
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray inventoryJsonArray = (JSONArray) jsonObject.get("inventory");
        String[] inventoryArray = new String[inventoryJsonArray.size()];
        for (int i = 0; i < inventoryArray.length; i++) {
            inventoryArray[i] = (String) inventoryJsonArray.get(i);
        }
        inventoryInstance.load(inventoryArray);
    }

    private static void parseMissionArray(JSONObject mission, List<MissionInfo> missionInfoList) {
        JSONArray agentArray=(JSONArray) mission.get("serialAgentsNumbers");
        List<String> serials= new LinkedList<String>();
        Iterator<String> it=agentArray.iterator();
        while (it.hasNext()){
            serials.add(it.next());
        }
        String gadget = (String) mission.get("gadget");
        int duration= Integer.parseInt(mission.get("duration").toString());
        String name= (String) mission.get("name");
        int timeIssued= Integer.parseInt(mission.get("timeIssued").toString());
        int timeExpired= Integer.parseInt(mission.get("timeExpired").toString());
        MissionInfo missionInfo =new MissionInfo(name,serials,gadget,timeIssued,timeExpired,duration);
        missionInfoList.add(missionInfo);

    }

    private static void parseSquadObject(JSONObject squad, HashMap<String, Agent> agents) {
        String name=(String) squad.get("name");
        String serialNumber=(String) squad.get("serialNumber");
        agents.put(serialNumber,new Agent(serialNumber,name));
    }
}
