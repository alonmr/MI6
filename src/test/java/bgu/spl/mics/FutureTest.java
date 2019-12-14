package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<String> future;
    private String result;
    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void test(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                result =future.get();
            }
        });
        t.start();
        future.resolve("Done");
        assertTrue(future.isDone());
        assertEquals(result,"Done");
    }

    @Test
    public void test2(){//if test2 passed and test didn't check wait and sync
        future.resolve("Done");
        String result = future.get();
        assertTrue(future.isDone());
        assertEquals(result,"Done");
    }
    @Test //this test depends on threads management
    public void test3(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                result =future.get(1000, TimeUnit.NANOSECONDS);
            }
        });
        t.start();
        future.resolve("Done");
        if(result != null)
            System.out.println(result);
    }
}
