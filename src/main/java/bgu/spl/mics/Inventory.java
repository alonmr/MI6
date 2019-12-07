package bgu.spl.mics;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Inventory {

    private static Inventory ourInstance;
    private LinkedList<String> gadgets;

    public static Inventory getInstance() {
        if (ourInstance == null) {
            ourInstance = new Inventory();
        }
        return ourInstance;
    }

    private Inventory() {
        ourInstance = new Inventory();
        gadgets = new LinkedList<>();
    }
    /**
     * @param gadget    	name of the gadget.
     * @return true if gadget is in the list and removes it
     */
    public boolean getItem(String gadget) {
        if(gadgets.contains(gadget)){
            gadgets.remove(gadget);
            return true;
        }
        return false;
    }

    //prints report to json file
    public void printToFile(String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            for (String gadget:gadgets) {
                file.write(gadget);
                //file.flush();
            }
        } catch (IOException e) {

        }
    }

    //initialize gadgets
    public void load(String[] gadgets) {
        for (String gadget :gadgets) {
            this.gadgets.add(gadget);
        }
    }
}
