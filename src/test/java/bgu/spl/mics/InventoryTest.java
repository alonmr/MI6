package bgu.spl.mics;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory ;
    @BeforeEach
    public void setUp(){
        inventory = Inventory.getInstance();
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader("/MI6/Input201.json"));
            String[] data = gson.fromJson(reader, String[].class); // contains the whole reviews list
            for(int index = 0 ; data[index] != "services" ; index++) {
            }
            //inventory.load(gadgets);

        } catch (IOException e) {

        }
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
    public void test2(){

        inventory.printToFile("/MI6/Output.json");
    }

}
