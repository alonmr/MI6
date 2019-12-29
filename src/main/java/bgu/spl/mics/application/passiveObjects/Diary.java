package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    private static class SingletonDiaryHolder {
        private static Diary instance = new Diary();
    }
    private Diary() {
        total = new AtomicInteger(0);
        reports = new LinkedList<Report>();
    }
    public static Diary getInstance() {
        return Diary.SingletonDiaryHolder.instance;
    }

    public void incrementTotal() {
        total.incrementAndGet();
    }

    public List<Report> getReports() {
        return null;
    }

    /**
     * adds a report to the diary
     *
     * @param reportToAdd - the report to add
     */
    public void addReport(Report reportToAdd) {
        synchronized (reports) {
            reports.add(reportToAdd);
        }
        incrementTotal();
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<Report> which is a
     * List of all the reports in the diary.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        JSONArray jsonReports = new JSONArray();
        for (Report report : reports) {
            JSONObject jsonObject = new JSONObject();
            JSONArray agentsNamesArray = new JSONArray();
            JSONArray agentsSerialNumbersArray = new JSONArray();
            agentsNamesArray.addAll(report.getAgentsNames());
            agentsSerialNumbersArray.addAll(report.getAgentsSerialNumbers());
            jsonObject.put("missionName", report.getMissionName());
            jsonObject.put("m", report.getM());
            jsonObject.put("moneyPenny", report.getMoneypenny());
            jsonObject.put("agentsSerialNumbers", agentsSerialNumbersArray);
            jsonObject.put("agentNames", agentsNamesArray);
            jsonObject.put("gadgetName", report.getGadgetName());
            jsonObject.put("timeCreated", report.getTimeCreated());
            jsonObject.put("timeIssued", report.getTimeIssued());
            jsonObject.put("QTime", report.getQTime());
            jsonReports.add(jsonObject);
        }
        JSONObject obj = new JSONObject();
        obj.put("reports", jsonReports);
        obj.put("total", getTotal());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(obj);
        try {
            FileWriter file = new FileWriter(filename);
            file.write(prettyJson);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the total number of received missions (executed / aborted) be all the M-instances.
     *
     * @return the total number of received missions (executed / aborted) be all the M-instances.
     */
    public AtomicInteger getTotal() {
        return total;
    }
}
