import java.util.LinkedList;
import java.util.Queue;

class ProcessThread extends Thread {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int initialBurstTime;
    int priority;
    int quantum;
    int waitingTime = 0;
    int turnaroundTime = 0;

    Queue<ProcessThread> queue = new LinkedList<>();

    public ProcessThread(String name, String color, int arrivalTime, int burstTime, int priority, int quantum, Queue<ProcessThread>q) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.initialBurstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.queue = q;
    }

        @Override
        public void run(){
            try {
                Thread.sleep(arrivalTime*1000l);
                queue.add(this);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


}
