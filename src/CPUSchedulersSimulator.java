import java.util.*;


public class CPUSchedulersSimulator {
    public static void main(String[] args) {

//        InputReader input = new InputReader();
//        List<Process> processes = input.read();

        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", "#0000FF", 0, 7, 1)); // Blue
        processes.add(new Process("P2", "#FF0000", 2, 4, 1)); // Red
        processes.add(new Process("P3", "#00FF00", 4, 1, 1)); // Green
        processes.add(new Process("P4", "#FFFF00", 5, 4, 1)); // Yellow

        // Test for Priority
//        processes.add(new Process("P1", "#0000FF", 0, 7, 4)); // Blue
//        processes.add(new Process("P2", "#FF0000", 0, 4, 1)); // Red
//        processes.add(new Process("P3", "#00FF00", 0, 1, 3)); // Green
//        processes.add(new Process("P4", "#FFFF00", 0, 4, 2)); // Yellow


        System.out.println("Select Scheduler: ");
        System.out.println("1 ====> SRTF ");
        System.out.println("2 ====> SJF  ");
        System.out.println("3 ====> Priority");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        System.out.println("\n");

        Scheduler scheduler;
        String schedulerName;

        switch (choice) {
            case 1:
                scheduler = new SRTFScheduler();
                schedulerName = "SRTF";
                break;
            case 2:
                scheduler = new SJFScheduler();
                schedulerName = "SJF";
                break;
            case 3:
                scheduler = new PriorityScheduler();
                schedulerName = "Priority";
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        scheduler.schedule(new ArrayList<>(processes));
        Map<String, Double> summary = scheduler.getSchedulingSummary();
        List<Execution> executionsSequence = scheduler.getExecutions();
        new SchedulerGUI(
                schedulerName,
                executionsSequence,
                summary.get("Average Waiting Time"),
                summary.get("Average Turnaround Time")
        );

    }

}
