package bgu.spl.mics.application;

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

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Inventory inventoryInstance = Inventory.getInstance();
        Squad squadInstance = Squad.getInstance();

        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader(args[0]));
            loadToInventory(obj, inventoryInstance);
            loadToSquad(obj, squadInstance);
            List<Intelligence> intelligenceList = new LinkedList<Intelligence>();
            addToIntelligenceList(obj, intelligenceList);
            JSONObject servicesObject = getServicesObject(obj);
            //initialize threads
            TimeService timeService = new TimeService(Integer.parseInt(servicesObject.get("time").toString()));
            Thread TS = new Thread(timeService);
            TS.start();
            ExecutorService intelligence = Executors.newFixedThreadPool(intelligenceList.size());
            for (int intelligenceSubscriber = 0; intelligenceSubscriber < intelligenceList.size(); intelligenceSubscriber++) {
                intelligence.execute(intelligenceList.get(intelligenceSubscriber));
            }
            intelligence.shutdown();
            int amountOfM = Integer.parseInt(servicesObject.get("M").toString());
            int amountOfMoneyPenny = Integer.parseInt(servicesObject.get("Moneypenny").toString());
            ExecutorService m = Executors.newFixedThreadPool(amountOfM);
            for (int mSubscriber = 0; mSubscriber < amountOfM; mSubscriber++) {
                m.execute(new M(mSubscriber + 1));
            }
            m.shutdown();
            ExecutorService moneyPenny = Executors.newFixedThreadPool(amountOfMoneyPenny );
            for (int moneypennySubscriber = 0; moneypennySubscriber < amountOfMoneyPenny; moneypennySubscriber++) {
                moneyPenny.execute(new Moneypenny(moneypennySubscriber + 1));
            }
            moneyPenny.shutdown();
            Thread Q = new Thread(new Q());
            Q.start();
            TS.join();//time service is done
            //terminate all
            Q.join();
            while (!m.isTerminated() | !moneyPenny.isTerminated() | !intelligence.isTerminated()) {
            }
            //join all then print to file
            Inventory.getInstance().printToFile(args[1]);
            Diary.getInstance().printToFile(args[2]);
        } catch (ParseException | IOException | InterruptedException ex) {
        }
    }

    private static JSONObject getServicesObject(Object obj) {
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject servicesObject = (JSONObject) jsonObject.get("services");
        return servicesObject;
    }

    private static void addToIntelligenceList(Object obj, List<Intelligence> intelligenceList) {
        JSONObject servicesObject = getServicesObject(obj);
        JSONArray intelligenceArray = (JSONArray) servicesObject.get("intelligence");
        for (int i = 0; i < intelligenceArray.size(); i++) {
            LinkedList<MissionInfo> missionInfoList = new LinkedList<MissionInfo>();
            JSONObject missionArray = (JSONObject) intelligenceArray.get(i);
            JSONArray insideMission = (JSONArray) missionArray.get("missions");
            insideMission.forEach(data -> parseMissionArray((JSONObject) data, missionInfoList));
            Intelligence intelligence = new Intelligence(missionInfoList, i + 1, Integer.parseInt(servicesObject.get("time").toString()));
            intelligenceList.add(intelligence);
        }
    }

    private static void loadToSquad(Object obj, Squad squadInstance) {
        JSONObject jsonObject = (JSONObject) obj;
        HashMap<String, Agent> agents = new HashMap<String, Agent>();
        JSONArray squadJsonArray = (JSONArray) jsonObject.get("squad");
        squadJsonArray.forEach(squad -> parseSquadObject((JSONObject) squad, agents));
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
        JSONArray agentArray = (JSONArray) mission.get("serialAgentsNumbers");
        List<String> serials = new LinkedList<String>();
        Iterator<String> it = agentArray.iterator();
        while (it.hasNext()) {
            serials.add(it.next());
        }
        String gadget = (String) mission.get("gadget");
        int duration = Integer.parseInt(mission.get("duration").toString());
        String name = (String) mission.get("name");
        int timeIssued = Integer.parseInt(mission.get("timeIssued").toString());
        int timeExpired = Integer.parseInt(mission.get("timeExpired").toString());
        MissionInfo missionInfo = new MissionInfo(name, serials, gadget, timeIssued, timeExpired, duration);
        missionInfoList.add(missionInfo);

    }

    private static void parseSquadObject(JSONObject squad, HashMap<String, Agent> agents) {
        String name = (String) squad.get("name");
        String serialNumber = (String) squad.get("serialNumber");
        agents.put(serialNumber, new Agent(serialNumber, name));
    }
}
