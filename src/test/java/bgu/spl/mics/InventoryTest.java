package bgu.spl.mics;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory ;
    @BeforeEach
    public void setUp(){
        inventory = Inventory.getInstance();
        Gson gson = new Gson();

        String[] gadgets = gson.fromJson("[\"Inventory\"]", String[].class);
        inventory.load(gadgets);
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

        fail("Not a good test");
    }

    @Test
    public void test3(){

        fail("Not a good test");
    }
}
