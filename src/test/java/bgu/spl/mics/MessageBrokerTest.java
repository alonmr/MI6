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



    @Test//checks if broadcast impl runs
    public void test2(){
        TickBroadcast b = new TickBroadcast(5);
        Q q = new Q();
        messageBrokerTest.register(q);
        messageBrokerTest.subscribeBroadcast(TickBroadcast.class,q);
        messageBrokerTest.sendBroadcast(b);
        messageBrokerTest.unregister(q);

    }
}
