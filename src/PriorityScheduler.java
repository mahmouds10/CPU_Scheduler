import java.util.*;

class PriorityScheduler implements Scheduler {
    private final Map<String, Double> schedulingSummary = new HashMap<>();
    private final List<Process> completed = new ArrayList<>();
    private final List<Execution> executions = new ArrayList<>();

    @Override
    public void schedule(List<Process> processes) {
        // Sort processes by arrival time and then priority
        processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                .thenComparingInt(p -> p.priority));

        // Tracks the current time in the simulation
        int currentTime = 0;

        // Priority queue to manage ready processes, sorted by priority
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt((Process p) -> p.priority).thenComparingInt(p -> p.arrivalTime)
        );

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue if they have arrived by the current time
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (readyQueue.isEmpty()) {
                // If no processes are ready, increment the current time to simulate idle CPU
                currentTime++;
                continue;
            }

            // Fetch the process with the highest priority (lowest priority number)
            Process current = readyQueue.poll();

            int startTime = currentTime;
            currentTime += current.burstTime; // Execute the process

            // Calculate waiting time (time spent waiting in the queue)
            current.waitingTime = startTime - current.arrivalTime;

            // Calculate turnaround time (total time from arrival to completion)
            current.turnaroundTime = currentTime - current.arrivalTime;

            // Add the current process to the completed list
            completed.add(current);

            // Log the execution details of the process
            executions.add(new Execution(current.name, "executes", startTime, currentTime, current.color));
            Logger.logExecution(current, startTime, currentTime);

            // Simulate context switching after each process execution
            Logger.logStop(current, currentTime);
        }

        ResultsCalculator calculator = new ResultsCalculator(completed);
        schedulingSummary.putAll(calculator.calc());
        Logger.logStatistics(completed);
    }

    @Override
    public List<Execution> getExecutions() {
        return executions;
    }

    @Override
    public Map<String, Double> getSchedulingSummary() {
        return schedulingSummary;
    }
}
