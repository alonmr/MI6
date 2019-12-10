package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad squad;
    @BeforeEach
    public void setUp() {
        squad = Squad.getInstance();
        HashMap<String, Agent> agents = new HashMap<String, Agent>();
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader("input201.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray squadArray = (JSONArray) jsonObject.get("squad");
            squadArray.forEach(squad -> parseSquadObject((JSONObject) squad, agents));

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        squad.load(agents);
    }
        @Test
    public void test(){
        List<String> list1=new LinkedList<>();
        list1.add("008");
        assertFalse(squad.getAgents(list1));
        List<String> list2=new LinkedList<>();
        list2.add("007");
        list2.add("006");
        assertTrue(squad.getAgents(list2));
        List<String> list3=new LinkedList<>();
        list3.add("007");
        list3.add("008");
        assertFalse(squad.getAgents(list3));

    }
        @Test
    public void test2(){
        List<String> list1=new LinkedList<>();
        list1.add("007");
        List<String> list=squad.getAgentsNames(list1);
        assertTrue(list.contains( "James Bond"));
        assertFalse(list.contains("Wannabe Agent"));
    }


    private void parseSquadObject(JSONObject squad,HashMap<String,Agent> agents) {
        String name=(String) squad.get("name");
        String serialNumber=(String) squad.get("serialNumber");
        agents.put(serialNumber,new Agent(serialNumber,name));
    }


}
