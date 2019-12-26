package bgu.spl.mics.application.passiveObjects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private List<Report> reports;
	private static Diary ourInstance;
	private AtomicInteger total;
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		if (ourInstance == null) {
			ourInstance = new Diary();
		}
		return ourInstance;
	}
	private Diary(){
		total=new AtomicInteger(0);
		reports=new LinkedList<Report>();
	}

	public void incrementTotal(){
		total.incrementAndGet();
	}

	public List<Report> getReports() {
		return null;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		synchronized (reports) {
			reports.add(reportToAdd);
		}
		incrementTotal();
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		try (FileWriter file = new FileWriter(filename)) {
			file.write("{"+"\n");
			file.write("\"reports\": ["+"\n");
			file.write(reports.size());
			for(int i=0;i<reports.size();i++) {


				for (Report report : reports) {
					file.write("{"+"\n");
					file.flush();
					file.write("\"missionName\": "+"\""+report.getMissionName()+"\",");
					file.write("\"m\": "+"\""+report.getM()+"\",");
					file.write("\"moneypenny\": "+"\""+report.getMoneypenny()+"\",");
					file.write("\"agentsSerialNumbers\": "+"[");
					file.write("\"agentsNames\": "+"[");
					file.write("\"gadgetName\": "+"\""+report.getGadgetName()+"\",");
					file.write("\"timeCreated\": "+"\""+report.getTimeCreated()+"\",");
					file.write("\"timeIssued\": "+"\""+report.getTimeIssued()+"\",");
					file.write("\"qTime\": "+"\""+report.getQTime()+"\",");
					file.write("},"+"\n");
					file.flush();
				}
				file.write("}" + "\n");
			}
		} catch (IOException e) {

		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public AtomicInteger getTotal(){
		return total;
	}
}
