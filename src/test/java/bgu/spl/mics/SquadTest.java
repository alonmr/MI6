package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import com.google.gson.Gson;
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
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
    private Squad squad;
    @BeforeEach
    public void setUp(){
        squad = Squad.getInstance();
        HashMap<String, Agent> agents=new HashMap<String, Agent>()  ;
        JSONParser jsonParser = new JSONParser();
        try{
            Object obj = jsonParser.parse(new FileReader("input201.json"));
            JSONObject jsonObject= (JSONObject) obj;
            JSONArray squadArray=(JSONArray) jsonObject.get("squad");
            squadArray.forEach( squad -> parseSquadObject( (JSONObject) squad, agents ) );

        }
        catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        squad.load(agents);
        @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}

    private void parseSquadObject(JSONObject squad,HashMap<String,Agent> agents) {
        String name=(String) squad.get("name");
        String serialNumber=(String) squad.get("serialNumber");
        agents.put(serialNumber,new Agent(serialNumber,name));
    }


}
