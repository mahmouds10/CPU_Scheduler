import java.util.*;

public class FCAIScheduler {
    public void schedule(List<ProcessThread> processes, QueueProcesses queue) {

        final double v1 = Collections.max(processes, Comparator.comparingInt(p -> p.arrivalTime)).arrivalTime / 10.0;
        final double v2 = Collections.max(processes, Comparator.comparingInt(p -> p.burstTime)).burstTime / 10.0;
        int currentTime = 0;
        int executionTime = 0;

        // Start all process threads
        for (ProcessThread process : processes) {
            process.queue = queue;
            process.start();
        }

        // loop for queue
        while (!queue.processQueue.isEmpty() || processes.stream().anyMatch(Thread::isAlive)) {
            ProcessThread currentProcess = null;
            double minFcaiFactor = Double.MAX_VALUE;

            // Find the process with the smallest FCAI factor
            for (ProcessThread process : queue.processQueue) {
                double fcaiFactor = calculateFCAI(process, v1, v2);
                if (fcaiFactor < minFcaiFactor || process.quantum == executionTime) {
                    minFcaiFactor = fcaiFactor;
                    currentProcess = process;
                }
            }

            // if process found
            if (currentProcess != null) {
                queue.processQueue.remove(currentProcess);  // remove it from queue

                int quantum = currentProcess.quantum;
                executionTime = (int) Math.ceil(quantum * 0.4); // 40% of quantum
//                System.out.println("\n-------current time  " + currentTime + "  ---------\n");

                System.out.printf("Executing %s (FCAI Factor: %.2f, Burst: %s Quantum: %d)\n", currentProcess.name,
                        minFcaiFactor, currentProcess.burstTime, quantum);


                try {
                    // if process didnt finish
                    if (currentProcess.burstTime >= executionTime) {
                        Thread.sleep(executionTime * 1000L);

                        // update burst and quantum
                        currentProcess.burstTime -= executionTime;
                        currentProcess.quantum += (currentProcess.quantum - executionTime); // Add unused quantum
                        System.out.println("\n-------burst time  " + currentProcess.burstTime + "  ---------\n");


                        System.out.printf("%s interrupted after executing 40%%, remaining burst time: %d, updated quantum: %d\n\n",
                                currentProcess.name, currentProcess.burstTime, currentProcess.quantum);

                        queue.processQueue.add(currentProcess);   // add it again to the queue
                        currentTime += executionTime;

                    }

                    else
                    {
                        Thread.sleep(currentProcess.burstTime * 1000L);
                        System.out.println("\n-------brst time  " + currentProcess.burstTime + "  ---------\n");
                        System.out.println("\n******* ex time  " + executionTime+ "  ****\n");

                        currentProcess.waitingTime = currentTime - (currentProcess.arrivalTime + currentProcess.initialBurstTime);
                        currentProcess.turnaroundTime = currentProcess.waitingTime + currentProcess.initialBurstTime;

                        System.out.printf("\nProcess %s completed: Waiting Time = %d, Turnaround Time = %d\n\n",
                                currentProcess.name, currentProcess.waitingTime, currentProcess.turnaroundTime);

//                        currentProcess.burstTime = 0;
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println("All processes have been scheduled.");
    }

    private double calculateFCAI(ProcessThread process, double v1, double v2) {
        return (10 - process.priority) + Math.ceil((process.arrivalTime / v1)) + Math.ceil((process.burstTime / v2));
    }
}
