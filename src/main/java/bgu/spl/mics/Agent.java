package bgu.spl.mics;

public class Agent {
    private String serialNumber;
    private String name;
    private boolean available;
    Agent(String serialNumber,String name){
        this.serialNumber=serialNumber;
        this.name=name;
        available=true;
    }
    public void acquire(){
        setAvailable(false);
    }
    public void release(){
        setAvailable(true);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getName(){
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
