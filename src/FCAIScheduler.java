import java.util.*;

public class FCAIScheduler {
    public void schedule(List<ProcessThread> processes, Queue<ProcessThread> queue) {
        List<Execution> executions = new ArrayList<>();
        final double v1 = Collections.max(processes, Comparator.comparingInt(p -> p.arrivalTime)).arrivalTime / 10.0;
        final double v2 = Collections.max(processes, Comparator.comparingInt(p -> p.burstTime)).burstTime / 10.0;

        int completionTime = 0;
        ProcessThread curr;

        // Start all process threads
        for (ProcessThread process : processes) {
            process.queue = queue;
            process.start();
        }

        while (!queue.isEmpty() || processes.stream().anyMatch(Thread::isAlive)) {
            curr = queue.poll();
            if (curr == null) continue;

            int rq = curr.quantum;
            int q = curr.quantum;
            double fcaiFac = calculateFCAI(curr, v1, v2);

            try {
                if (curr.burstTime > 0) {
                    System.out.printf("Executing %s (FCAI Factor: %.2f, Burst: %s Quantum: %d)\n", curr.name, fcaiFac, curr.burstTime, rq);

                    int startTime = completionTime; // Start of execution
                    while (((q - rq + 1) / (double) q) <= 0.4) {
                        rq--;
                        if (curr.burstTime > 0) {
                            Thread.sleep(1000);
                            curr.burstTime--;
                            completionTime++;
                        }
                    }

                    while (rq > 0) {
                        Optional<ProcessThread> lowerFcaiProcess = queue.stream()
                                .filter(p -> calculateFCAI(p, v1, v2) < fcaiFac)
                                .findFirst();

                        if (lowerFcaiProcess.isPresent()) {
                            ProcessThread process = lowerFcaiProcess.get();
                            queue.remove(process);
                            ((LinkedList<ProcessThread>) queue).addFirst(process);
                            break;
                        }

                        rq--;
                        if (curr.burstTime > 0) {
                            Thread.sleep(1000);
                            curr.burstTime--;
                            completionTime++;
                        }
                    }

                    int endTime = completionTime; // End of execution
                    executions.add(new Execution(curr.name, "executes", startTime, endTime, curr.color));

                    if (rq > 0)
                        curr.quantum += rq; // Add unused quantum
                    else
                        curr.quantum += 2;

                    if (curr.burstTime == 0) {
                        curr.waitingTime = completionTime - (curr.arrivalTime + curr.initialBurstTime);
                        curr.turnaroundTime = curr.waitingTime + curr.initialBurstTime;

                        System.out.printf("\nProcess %s completed: Waiting Time = %d, Turnaround Time = %d\n\n",
                                curr.name, curr.waitingTime, curr.turnaroundTime);

                    } else {
                        queue.add(curr);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Calculate stats and send to GUI
        double totalWaiting = processes.stream().mapToDouble(p -> p.waitingTime).sum();
        double totalTurnaround = processes.stream().mapToDouble(p -> p.turnaroundTime).sum();
        double avgWaitingTime = totalWaiting / processes.size();
        double avgTurnaroundTime = totalTurnaround / processes.size();

        new SchedulerGUI("FCAI Scheduler", executions, avgWaitingTime, avgTurnaroundTime);
    }

    private double calculateFCAI(ProcessThread process, double v1, double v2) {
        return (10 - process.priority) + Math.ceil((process.arrivalTime / v1)) + Math.ceil((process.burstTime / v2));
    }
}
