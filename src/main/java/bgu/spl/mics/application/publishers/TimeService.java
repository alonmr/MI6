package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import static java.lang.Thread.sleep;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

    private int Tick;
    private int duration;

    public TimeService(int duration) {
        super("Time");
        Tick = 0;
        this.duration = duration;
    }

    @Override
    protected void initialize() {
    }

    @Override
    public void run() {
        SimplePublisher SP = getSimplePublisher();
        while (Tick < duration) {
            try {
                sleep(100);
                Tick += 1;
            } catch (InterruptedException ignored) {
            }
            SP.sendBroadcast(new TickBroadcast(Tick));
        }
        SP.sendBroadcast(new TerminateBroadcast());
    }
}
