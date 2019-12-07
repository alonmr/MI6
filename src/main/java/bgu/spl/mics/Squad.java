package bgu.spl.mics;

public class Squad {
    private static Squad ourInstance = new Squad();

    public static Squad getInstance() {
        return ourInstance;
    }

    private Squad() {
    }
}
