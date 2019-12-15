package bgu.spl.mics;


import bgu.spl.mics.application.passiveObjects.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory ;
    @BeforeEach
    public void setUp(){
        inventory = Inventory.getInstance();
        String inventoryArray []=new String[4];//Assuming config file is input201.json
        JSONParser jsonParser = new JSONParser();
        try{
            Object obj = jsonParser.parse(new FileReader("input201.json"));
            JSONObject jsonObject= (JSONObject) obj;
            JSONArray inventoryJsonArray=(JSONArray)jsonObject.get("inventory");
            inventoryArray=new String[inventoryJsonArray.size()];
            Iterator<String> it=inventoryJsonArray.iterator();
            int arrayIndex=0;
            while (it.hasNext()) {
                inventoryArray[arrayIndex]=it.next();
                arrayIndex++;
            }

        } catch (IOException | ParseException e) {}
        inventory.load(inventoryArray);
    }

    @Test
    public void test(){
        assertTrue(inventory.getItem("Sky Hook"));
        assertFalse(inventory.getItem("Sky Hook"));
        assertTrue(inventory.getItem("Geiger counter"));
        assertFalse(inventory.getItem("Geiger counter"));
        assertTrue(inventory.getItem("X-ray glasses"));
        assertFalse(inventory.getItem("X-ray glasses"));
        assertTrue(inventory.getItem("Dagger shoe"));
        assertFalse(inventory.getItem("Dagger shoe"));
        assertFalse(inventory.getItem("Sky Hook"));
    }

    @Test
    public void test2(){//check if writes to output.json
        inventory.printToFile("Output.json");

    }

}
