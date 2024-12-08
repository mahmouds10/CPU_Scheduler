import java.util.List;

class logger {
    public static void printExecution(Process process, int startTime, int endTime) {
        System.out.printf("%s executes from %d to %d (waiting time: %d, turnaround time: %d)\n",
                process.name, startTime, endTime, process.waitingTime, process.turnaroundTime);
    }

    public static void printStatistics(List<Process> processes) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        StringBuilder waitingTimeExpression = new StringBuilder();
        StringBuilder turnaroundTimeExpression = new StringBuilder();

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;

            waitingTimeExpression.append(process.waitingTime);
            turnaroundTimeExpression.append(process.turnaroundTime);

            if (i < processes.size() - 1) {
                waitingTimeExpression.append(" + ");
                turnaroundTimeExpression.append(" + ");
            }
        }

        System.out.println("\nStatistics:");
        System.out.printf("Average Waiting Time: (%s) / %d = %.2f\n",
                waitingTimeExpression, processes.size(), totalWaitingTime / processes.size());
        System.out.printf("Average Turnaround Time: (%s) / %d = %.2f\n",
                turnaroundTimeExpression, processes.size(), totalTurnaroundTime / processes.size());
    }
}
