import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsCalculator {
    private final List<Process> processes ;

    public ResultsCalculator(List<Process> processes) {
        this.processes = processes;
    }

    public Map<String, Double> calc() {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        double averageTurnaroundTime = 0;
        double averageWaitingTime = 0;

        // Calculate total waiting time and turnaround time
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        // Calculate averages
        if (!processes.isEmpty()) {
            averageWaitingTime = totalWaitingTime / processes.size();
            averageTurnaroundTime = totalTurnaroundTime / processes.size();
        }

        // Store all results in a HashMap
        Map<String, Double> result = new HashMap<>();
        result.put("Total Waiting Time", totalWaitingTime);
        result.put("Total Turnaround Time", totalTurnaroundTime);
        result.put("Average Waiting Time", averageWaitingTime);
        result.put("Average Turnaround Time", averageTurnaroundTime);

        // Return the HashMap
        return result;
    }
}
