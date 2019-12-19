package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        Inventory inventory = Inventory.getInstance();
        String inventoryArray []=new String[4];//Assuming config file is input201.json
        JSONParser jsonParser = new JSONParser();
        try{
            Object obj = jsonParser.parse(new FileReader(String.valueOf(args)));
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
        // TODO Implement this
    }
}
