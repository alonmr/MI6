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

	private Map<String, Agent> agentsMap;
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
     * @param agents    Data structure containing all data necessary for initialization
     * 						of the squad.
     */
	public void load (HashMap<String, Agent> agents) {
		if (agentsMap == null)
			agentsMap = new HashMap<>();
		agentsMap.putAll(agents); //Copies agents elements to agents HashMap.
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		synchronized (this) {
			ListIterator<String> serialIterator = serials.listIterator();
			while (serialIterator.hasNext()) {
				agentsMap.get(serialIterator.next()).release();
			}
			notifyAll();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){ //simulates execution of a mission, agents should be already acquired.
		try {
				sleep(time*100);
			} catch (Exception ignored) {
			}
			releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
		synchronized (agentsMap) {
			ListIterator<String> serialIterator = serials.listIterator();
			while (serialIterator.hasNext()) {
				String serialNum = serialIterator.next();
				if (!agentsMap.containsKey(serialNum)) {
					System.out.println(serialNum+" is not in squad");
					return false;

				}
			}
			acquireAgents(serials);
		}
		return true;
	}

	private void acquireAgents(List<String> serials) {
		ListIterator<String> serialIterator = serials.listIterator();
		while (serialIterator.hasNext()) {
			String serialNum = serialIterator.next();
			Agent agent = agentsMap.get(serialNum);
			while(!agent.isAvailable()){
				try{wait();
					}
				catch (Exception ignored){}
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
			if (agentsMap.containsKey(serialNum)) {
				namesList.add(agentsMap.get(serialNum).getName());
			}
		}
		return namesList;
    }

}
