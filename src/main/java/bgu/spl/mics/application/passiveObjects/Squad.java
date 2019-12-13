package bgu.spl.mics.application.passiveObjects;
import java.util.*;

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
			agents = new HashMap<String, Agent>();
		agents.putAll(inventory); //Copies inventory elements to agents HashMap.
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		ListIterator<String> serialIterator = serials.listIterator();
		while(serialIterator.hasNext()){
			if(agents.containsKey(serialIterator.next())){
				agents.get(serialIterator).release();//TODO: check if it works because get.
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
		ListIterator<String> serialIterator = serials.listIterator();
		while (serialIterator.hasNext()) {
			if (!agents.containsKey(serialIterator.next())) {
				return false;
			}
		}
		return true; //TODO:Method should acquire agents before
	}// If an agent is in the Squad, but is already acquired for some
	// other mission, the function will wait until the agent becomes available

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
		List<String> namesList=new LinkedList<>();
		ListIterator<String> serialIterator = serials.listIterator();
		while (serialIterator.hasNext()) {
			if (agents.containsKey(serialIterator.next())) {
				namesList.add(agents.get(serialIterator).getName());
			}
		}
		return namesList;
    }

}
