import java.util.*;

class SJFScheduler implements Scheduler {

    public Map<String, Double> SchedulingSummary;
    // List to store completed processes for later analysis and statistics
    List<Process> completed = new ArrayList<>();

    @Override
    public List<Process> schedule(List<Process> processes) {
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

            // Calculate waiting time (time spent waiting in the queue)
            current.waitingTime = currentTime - current.arrivalTime;

            // Calculate turnaround time (total time from arrival to completion)
            current.turnaroundTime = current.waitingTime + current.burstTime;

            // Update the current time after the process finishes execution
            int startTime = currentTime;
            currentTime += current.burstTime;

            // Add the process to the list of completed processes
            completed.add(current);

            // Log the execution details of the process
            logger.printExecution(current, startTime, currentTime);

        }

        // Log the final statistics (average waiting and turnaround times)
        logger.printStatistics(completed);

        ResultsCalculator calculator = new ResultsCalculator(completed);

        SchedulingSummary = calculator.calc();
        return completed;
    }
}