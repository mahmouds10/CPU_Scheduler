import java.util.*;

class SJFScheduler implements Scheduler {
    private final Map<String, Double> schedulingSummary = new HashMap<>();
    private final List<Process> completed = new ArrayList<>();
    private final List<Execution> executions = new ArrayList<>();

    @Override
    public void schedule(List<Process> processes) {

        // Sort processes based on arrival time to ensure they are considered in the correct order
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        // Priority queue to manage ready processes, sorted by burst time for SJF scheduling
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt((Process p) -> p.burstTime).thenComparingInt(p -> p.arrivalTime)
        );

        // Tracks the current time in the simulation
        int currentTime = 0;

        // Continue until all processes are either completed or waiting in the ready queue
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue if they have arrived by the current time
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            // If no processes are ready, increment the current time to simulate idle CPU
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Fetch the process with the shortest burst time from the ready queue
            Process current = readyQueue.poll();

            int startTime = currentTime;
            currentTime += current.burstTime;

            // Calculate waiting time (time spent waiting in the queue)
            current.waitingTime = startTime - current.arrivalTime;

            // Calculate turnaround time (total time from arrival to completion)
            current.turnaroundTime = currentTime - current.arrivalTime;

            // Add the current process to the completed list
            completed.add(current);

            // Add the current to execution sequence
            executions.add(new Execution(current.name, "executes", startTime, currentTime, current.color));

            // Log the execution details of the process
            Logger.logExecution(current, startTime, currentTime);
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