import java.util.*;

public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        Map<String, List<Process>> processesMap = initializeProcessesMap();
        List<ProcessThread> FCAIProcesses = initializeFCAIProcesses();
        Queue<ProcessThread> queue = new LinkedList<>();

        displayMenu();
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        handleSchedulerChoice(choice, processesMap, FCAIProcesses, queue);
    }

    private static Map<String, List<Process>> initializeProcessesMap() {
        Map<String, List<Process>> processesMap = new HashMap<>();

        // SJF and SRTF Processes
        List<Process> sjfSrtfProcesses = Arrays.asList(
                new Process("P1", "#0000FF", 0, 7, 1),
                new Process("P2", "#FF0000", 2, 4, 1),
                new Process("P3", "#00FF00", 4, 1, 1),
                new Process("P4", "#FFFF00", 5, 4, 1)
        );
        processesMap.put("SJF_SRTF", sjfSrtfProcesses);

        // Priority Processes
        List<Process> priorityProcesses = Arrays.asList(
                new Process("P1", "#0000FF", 0, 10, 3),
                new Process("P2", "#FF0000", 0, 1, 1),
                new Process("P3", "#00FF00", 0, 2, 4),
                new Process("P4", "#FFFF00", 0, 1, 5),
                new Process("P5", "#006699", 0, 5, 2)
        );
        processesMap.put("Priority", priorityProcesses);

        return processesMap;
    }

    private static List<ProcessThread> initializeFCAIProcesses() {
        Queue<ProcessThread> queue = new LinkedList<>();
        return Arrays.asList(
                new ProcessThread("P1", "#FF0000", 0, 17, 4, 4, queue),
                new ProcessThread("P2", "#0000FF", 3, 6, 9, 3, queue),
                new ProcessThread("P3", "#00FF00", 4, 10, 3, 5, queue),
                new ProcessThread("P4", "#FFFF00", 29, 4, 10, 2, queue)
        );
    }

    private static void displayMenu() {
        System.out.println("Select Scheduler: ");
        System.out.println("1 ====> SRTF ");
        System.out.println("2 ====> SJF  ");
        System.out.println("3 ====> Priority");
        System.out.println("4 ====> FCAI");
    }

    private static void handleSchedulerChoice(int choice, Map<String, List<Process>> processesMap, List<ProcessThread> FCAIProcesses, Queue<ProcessThread> queue) {
        switch (choice) {
            case 1:
                executeStandardScheduler(new SRTFScheduler(), "SRTF", processesMap.get("SJF_SRTF"));
                break;
            case 2:
                executeStandardScheduler(new SJFScheduler(), "SJF", processesMap.get("SJF_SRTF"));
                break;
            case 3:
                executeStandardScheduler(new PriorityScheduler(), "Priority", processesMap.get("Priority"));
                break;
            case 4:
                executeFCAIScheduler(FCAIProcesses, queue);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void executeStandardScheduler(Scheduler scheduler, String schedulerName, List<Process> processes) {
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

    private static void executeFCAIScheduler(List<ProcessThread> FCAIProcesses, Queue<ProcessThread> queue) {
        FCAIScheduler FcaiScheduler = new FCAIScheduler();
        FcaiScheduler.schedule(FCAIProcesses, queue);

        double totalWaiting = FCAIProcesses.stream().mapToDouble(p -> p.waitingTime).sum();
        double totalTurnaround = FCAIProcesses.stream().mapToDouble(p -> p.turnaroundTime).sum();
        double averageWaiting = totalWaiting / FCAIProcesses.size();
        double averageTurnaround = totalTurnaround / FCAIProcesses.size();

        System.out.printf("\nTotal Waiting = %.2f\n", totalWaiting);
        System.out.printf("Total Turnaround = %.2f\n", totalTurnaround);
        System.out.printf("Average Waiting = %.2f\n", averageWaiting);
        System.out.printf("Average Turnaround = %.2f\n", averageTurnaround);
    }
}
