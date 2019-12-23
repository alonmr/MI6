package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
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
        String[] inventoryArray=new String[0] ;
        JSONParser jsonParser = new JSONParser();
        try{
            Object obj = jsonParser.parse(new FileReader("input201.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray inventoryJsonArray = (JSONArray) jsonObject.get("inventory");
            inventoryArray = new String[inventoryJsonArray.size()];
            for (int i = 0; i < inventoryArray.length; i++) {
                inventoryArray[i] = (String) inventoryJsonArray.get(i);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        inventory.load(inventoryArray);
        // TODO Implement this
    }
}
