import java.util.*;


public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", "#0000FF", 0, 7, 1)); // Blue
        processes.add(new Process("P2", "#FF0000", 2, 4, 1)); // Red
        processes.add(new Process("P3", "#00FF00", 4, 1, 1)); // Green
        processes.add(new Process("P4", "#FFFF00", 5, 4, 1)); // Yellow

        // SJF
        SJFScheduler scheduler = new SJFScheduler();
        List<Process> completedProcesses = scheduler.schedule(processes);
        new SJFSchedulerGUI(completedProcesses, scheduler.SchedulingSummary.get("Average Waiting Time"), scheduler.SchedulingSummary.get("Average Turnaround Time"));



    }


//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the number of processes: ");
//        int numProcesses = scanner.nextInt();
//
//        List<Process> processes = new ArrayList<>();
//        for (int i = 0; i < numProcesses; i++) {
//            System.out.println("Enter details for Process " + (i + 1) + ":");
//            System.out.print("Name: ");
//            String name = scanner.next();
//            System.out.print("Color: ");
//            String color = scanner.next();
//            System.out.print("Arrival Time: ");
//            int arrivalTime = scanner.nextInt();
//            System.out.print("Burst Time: ");
//            int burstTime = scanner.nextInt();
//            System.out.print("Priority: ");
//            int priority = scanner.nextInt();
//
//            processes.add(new Process(name, color, arrivalTime, burstTime, priority));
//        }
//
//        System.out.println("Select Scheduler: 1. Priority 2. SJF 3. FCAI");
//        int choice = scanner.nextInt();
//
//        Scheduler scheduler;
//        switch (choice) {
//            case 1:
//                scheduler = new PriorityScheduler();
//                break;
//            case 2:
//                scheduler = new SJFScheduler();
//                break;
//            case 3:
//                scheduler = new FCAIScheduler();
//                break;
//            default:
//                System.out.println("Invalid choice.");
//                return;
//        }
//
//        scheduler.schedule(processes);
//    }
}
