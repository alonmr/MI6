package bgu.spl.mics;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
    private Squad squad;
    @BeforeEach
    public void setUp(){
        squad = Squad.getInstance();
        Gson gson = new Gson();

        String[] squad = gson.fromJson("[\"Squad\"]", String[].class);
        squad.load(squad);
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }
}
