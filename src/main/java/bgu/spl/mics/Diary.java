package bgu.spl.mics;

public class Diary {
    private static Diary ourInstance = new Diary();

    public static Diary getInstance() {
        return ourInstance;
    }

    private Diary() {
    }
}
