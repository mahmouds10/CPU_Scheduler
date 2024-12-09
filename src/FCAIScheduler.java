import java.util.*;

public class FCAIScheduler {
    public void schedule(List<ProcessThread> processes, Queue<ProcessThread> queue) {

        final double v1 = Collections.max(processes, Comparator.comparingInt(p -> p.arrivalTime)).arrivalTime / 10.0;
        final double v2 = Collections.max(processes, Comparator.comparingInt(p -> p.burstTime)).burstTime / 10.0;
        int completionTime = 0;
        long executionTime = 0;


        // Start all process threads
        for (ProcessThread process : processes) {
            process.queue = queue;
            process.start();
        }

        // loop for queue
        while (!queue.isEmpty() || processes.stream().anyMatch(Thread::isAlive)) {
            ProcessThread currentProcess = null;
            double minFcaiFactor = Double.MAX_VALUE;

            // Find the process with the smallest FCAI factor
            for (ProcessThread process : queue) {
                double fcaiFactor = calculateFCAI(process, v1, v2);
                if (fcaiFactor < minFcaiFactor) {
                    minFcaiFactor = fcaiFactor;
                    currentProcess = process;
                }
            }
            queue.remove(currentProcess);  // remove it from queue

            //_______________________________________________________________________________________________

            // if process found
            try {
                if (currentProcess.burstTime > 0) {


                    int remainQuan = currentProcess.quantum;
                    System.out.printf("Executing %s (FCAI Factor: %.2f,  Burst: %s  Quantum: %d)\n", currentProcess.name, minFcaiFactor, currentProcess.burstTime, remainQuan);

                    while ((remainQuan/(double)currentProcess.quantum) >= 0.4)
                    {
                        remainQuan--;
                        if(currentProcess.burstTime > 0){
                            Thread.sleep(1000);
                            currentProcess.burstTime--;
                            completionTime++;}

                    }
                    if(remainQuan > 0)
                    {
                        currentProcess.quantum += remainQuan;  // Add unused quantum
                    }
                    else
                    {
                        currentProcess.quantum += 2;

                    }
                    System.out.printf("%s interrupted after executing 40%%, remaining burst time: %d, updated quantum: %d\n\n", currentProcess.name, currentProcess.burstTime, currentProcess.quantum);
                    queue.add(currentProcess);   // add it again to the queue

                }
                else
                {
//                    Thread.sleep(2000L);

                    System.out.println(completionTime);
                    currentProcess.waitingTime = completionTime - (currentProcess.arrivalTime + currentProcess.initialBurstTime);
                    currentProcess.turnaroundTime = currentProcess.waitingTime + currentProcess.initialBurstTime;

                    System.out.printf("\nProcess %s completed: Waiting Time = %d, Turnaround Time = %d\n\n",
                            currentProcess.name, currentProcess.waitingTime, currentProcess.turnaroundTime);


                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All processes have been scheduled.");
    }

    private double calculateFCAI(ProcessThread process, double v1, double v2) {
        return (10 - process.priority) + Math.ceil((process.arrivalTime / v1)) + Math.ceil((process.burstTime / v2));
    }
}

