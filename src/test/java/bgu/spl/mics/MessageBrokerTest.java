package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private MessageBroker massageBrokerTest;
    @BeforeEach
    public void setUp(){
        massageBrokerTest = MessageBrokerImpl.getInstance();

        massageBrokerTest.subscribeEvent(MissionReceivedEvent(mission));
    }

    @Test
    public void test(){
        boolean thrown=false;
        M M1 = new M();
        M M2 = new M();
        massageBrokerTest.register(M1);
        try{
            massageBrokerTest.awaitMessage(M1);
        } catch (InterruptedException e) {
            thrown=true;
        }
        assertFalse(thrown);
        try{
            massageBrokerTest.awaitMessage(M2);
        } catch (InterruptedException e) {
            thrown=true;
        }
        assertTrue(thrown);

    }
}
