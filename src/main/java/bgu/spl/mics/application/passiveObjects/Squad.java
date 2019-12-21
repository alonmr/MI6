package bgu.spl.mics.application.passiveObjects;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	private static Squad ourInstance;
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		if (ourInstance == null) {
			ourInstance = new Squad();
		}
		return ourInstance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
     * @param inventory    Data structure containing all data necessary for initialization
     * 						of the squad.
     */
	public void load (HashMap<String, Agent> inventory) {
		if (agents == null)
			agents = new HashMap<>();
		agents.putAll(inventory); //Copies inventory elements to agents HashMap.
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		ListIterator<String> serialIterator = serials.listIterator();
		while(serialIterator.hasNext()){
			agents.get(serialIterator.next()).release();//TODO: check if it works because get.
		}

	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		synchronized (this) {

			ListIterator<String> serialIterator1 = serials.listIterator();
			while (serialIterator1.hasNext()) {
				agents.get(serialIterator1.next()).acquire();
			}
			try {
				sleep(time);
			} catch (Exception ignored) {
			}
			releaseAgents(serials);
		}
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
		synchronized (agents) {
			ListIterator<String> serialIterator = serials.listIterator();
			while (serialIterator.hasNext()) {
				String serialNum = serialIterator.next();
				if (agents.containsKey(serialNum)) {
				} else
					return false;
			}
			acquireAgents(serials);
		}
		return true;//TODO:Method should acquire agents before
	}

	private void acquireAgents(List<String> serials) {
		ListIterator<String> serialIterator = serials.listIterator(); //TODO:Ask Eizenberg about sync a function in a function
		while (serialIterator.hasNext()) {
			String serialNum = serialIterator.next();
			Agent agent = agents.get(serialNum);
			while(!agent.isAvailable()){
				try{wait();}
				catch (Exception ignored){};
			}
			agent.acquire();
		}
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		List<String> namesList=new LinkedList<>();
		ListIterator<String> serialIterator = serials.listIterator();
		while (serialIterator.hasNext()) {
			String serialNum = serialIterator.next();
			if (agents.containsKey(serialNum)) {
				namesList.add(agents.get(serialNum).getName());
			}
		}
		return namesList;
    }

}
