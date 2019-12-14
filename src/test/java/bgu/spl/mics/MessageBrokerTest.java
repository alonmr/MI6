package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    private MessageBroker MB;
    @BeforeEach
    public void setUp(){
        MB = MessageBrokerImpl.getInstance();
        M M = new M();
        MB.register(M);
    }

    @Test
    public void test(){


    }
}
