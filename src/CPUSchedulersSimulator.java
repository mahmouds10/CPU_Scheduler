import java.util.*;


public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", "#0000FF", 0, 7, 1)); // Blue
        processes.add(new Process("P2", "#FF0000", 2, 4, 1)); // Red
        processes.add(new Process("P3", "#00FF00", 4, 1, 1)); // Green
        processes.add(new Process("P4", "#FFFF00", 5, 4, 1)); // Yellow

        // SJF
        SJFScheduler scheduler = new SJFScheduler();
        List<Process> completedProcesses = scheduler.schedule(processes);
        new SJFSchedulerGUI(completedProcesses, scheduler.SchedulingSummary.get("Average Waiting Time"), scheduler.SchedulingSummary.get("Average Turnaround Time"));



    }
}
