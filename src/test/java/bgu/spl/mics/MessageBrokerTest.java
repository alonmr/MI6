package bgu.spl.mics;

import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Q;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private MessageBroker messageBrokerTest;
    @BeforeEach
    public void setUp(){
        messageBrokerTest = MessageBrokerImpl.getInstance();

    }

    @Test
    public void test(){
        boolean thrown=false;
        M M1 = new M();
        Thread M = new Thread(M1);
        M.start();
        MissionReceivedEvent event = new MissionReceivedEvent("Intelli");
        try{
            M.join();
            messageBrokerTest.sendEvent(event);
            messageBrokerTest.awaitMessage(M1);
        } catch (InterruptedException e) {
            thrown=true;
        }
        assertFalse(thrown);
    }

    @Test//checks if broadcast impl runs
    public void test2(){
        TickBroadcast b = new TickBroadcast("TimeService");
        M M1 = new M();
        Q q = new Q();
        messageBrokerTest.register(M1);
        messageBrokerTest.register(q);
        messageBrokerTest.subscribeBroadcast(TickBroadcast.class,M1);
        messageBrokerTest.subscribeBroadcast(TickBroadcast.class,q);
        messageBrokerTest.sendBroadcast(b);
        messageBrokerTest.unregister(q);

    }

    @Test
    public void test3(){
        M M1 = new M();
        MissionReceivedEvent e = new MissionReceivedEvent("intelligence");
        messageBrokerTest.subscribeEvent(MissionReceivedEvent.class,M1);
        Future<String> future = messageBrokerTest.sendEvent(e);
        messageBrokerTest.complete(e,"resolved");
        assertEquals("resolved",future.get());
    }
}
