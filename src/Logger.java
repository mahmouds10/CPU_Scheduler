import java.util.List;

class Logger {

    public static void logExecution(Process process, int startTime, int endTime) {
        String message = String.format("%s executes from %d to %d", process.name, startTime, endTime);
        System.out.println(message);
    }

    public static void logStop(Process process, int time) {
        String message = String.format("%s stops at time %d", process.name, time);
        System.out.println(message);
    }

    public static void logResume(Process process, int time) {
        String message = String.format("%s resumes at time %d", process.name, time);
        System.out.println(message);
    }

    public static void logCompletion(Process process, int time) {
        // Ensure that process.turnaroundTime and process.waitingTime are properly calculated before logging
        String message = String.format("%s completes at time %d (waiting time: %d, turnaround time: %d)",
                process.name, time, process.waitingTime, process.turnaroundTime);
        System.out.println(message);
    }

    public static void logStatistics(List<Process> processes) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        String stats = String.format("Average Waiting Time: %.2f\nAverage Turnaround Time: %.2f",
                avgWaitingTime, avgTurnaroundTime);
        System.out.println("\n================================");
        System.out.println(stats);
    }
}
