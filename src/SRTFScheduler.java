import java.util.*;

class SRTFScheduler implements Scheduler {
    private final Map<String, Double> schedulingSummary = new HashMap<>();
    private final List<Process> completed = new ArrayList<>();
    private final List<Execution> executions = new ArrayList<>();

    @Override
    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort processes by arrival time
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt((Process p) -> p.burstTime)
                .thenComparingInt(p -> p.arrivalTime));

        // Keep original burst times for calculating turnaround and waiting times later
        Map<String, Integer> originalBurstTimes = new HashMap<>();
        for (Process process : processes) {
            originalBurstTimes.put(process.name, process.burstTime);
        }

        int currentTime = 0; // Tracks current simulation time
        Process currentProcess = null; // The process currently being executed
        int processStartTime = -1; // The start time of the current process

        while (!processes.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            // Add newly arrived processes to the ready queue
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            // Preempt the current process if a shorter remaining time process arrives
            if (currentProcess != null) {
                if (!readyQueue.isEmpty() && readyQueue.peek().burstTime < currentProcess.burstTime) {
                    // Log process preemption (stop current process)
                    Logger.logStop(currentProcess, currentTime);
                    executions.add(new Execution(currentProcess.name, "preempted", processStartTime, currentTime, currentProcess.color));
                    readyQueue.add(currentProcess);  // Add preempted process back to the queue
                    currentProcess = null; // Clear current process
                }
            }

            // Select the next process to execute
            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                Logger.logResume(currentProcess, currentTime); // Log when a process starts/resumes
                processStartTime = currentTime; // Record when this process starts/resumes
            }

            // Execute the current process for one unit of time
            if (currentProcess != null) {
                // Only log when the process starts executing
                if (currentProcess.burstTime == originalBurstTimes.get(currentProcess.name)) {
                    Logger.logExecution(currentProcess, currentTime, currentTime + 1);
                }

                currentProcess.burstTime--;
                currentTime++;

                // If the current process finishes, log its completion
                if (currentProcess.burstTime == 0) {
                    executions.add(new Execution(currentProcess.name, "completed", processStartTime, currentTime,currentProcess.color));
                    // Calculate turnaround and waiting times after the process completes
                    currentProcess.turnaroundTime = currentTime - currentProcess.arrivalTime;  // turnaroundTime = finishTime - arrivalTime
                    currentProcess.waitingTime = currentProcess.turnaroundTime - originalBurstTimes.get(currentProcess.name); // waitingTime = turnaroundTime - burstTime
                    completed.add(currentProcess);
                    Logger.logCompletion(currentProcess, currentTime); // Log process completion
                    currentProcess = null; // Clear the current process after completion
                }
            } else {
                currentTime++; // Increment time for idle CPU if no process is running
            }
        }

        // Calculate and log scheduling statistics
        ResultsCalculator calculator = new ResultsCalculator(completed);
        schedulingSummary.putAll(calculator.calc());
        Logger.logStatistics(completed); // Log overall statistics
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
