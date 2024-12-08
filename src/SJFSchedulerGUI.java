import javax.swing.*;
import java.awt.*;
import java.util.List;

class SJFSchedulerGUI {

    private final List<Process> completedProcesses;
    private final double avgWaitingTime;
    private final double avgTurnaroundTime;

    public SJFSchedulerGUI(List<Process> completedProcesses, double avgWaitingTime, double avgTurnaroundTime) {
        this.completedProcesses = completedProcesses;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
        initializeGUI();
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("SJF Scheduler Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main container panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Gantt Chart Panel
        JPanel ganttPanel = new JPanel();
        ganttPanel.setLayout(null); // Use null layout for precise positioning
        ganttPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));

        // Calculate total burst time
        int totalBurstTime = completedProcesses.stream().mapToInt(p -> p.burstTime).sum();

        // Track the x position for process panels
        int currentX = 0;

        // Screen width for Gantt Chart
        int chartWidth = 750; // Leave some padding
        int chartHeight = 100; // Fixed height for bars

        for (Process process : completedProcesses) {
            // Calculate the width of the process panel
            int processWidth = (int) ((process.burstTime / (double) totalBurstTime) * chartWidth);

            // Create the process panel
            JPanel processPanel = new JPanel();
            processPanel.setBounds(currentX, 50, processWidth, chartHeight);
            processPanel.setBackground(Color.decode(process.color));
            processPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Add process label
            JLabel processLabel = new JLabel(process.name);
            processLabel.setForeground(Color.WHITE); // Ensure visibility
            processPanel.add(processLabel);

            // Add to the Gantt chart panel
            ganttPanel.add(processPanel);

            // Update x position for the next process
            currentX += processWidth;
        }

        // Statistics Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        JLabel avgWaitingLabel = new JLabel(String.format("Average Waiting Time: %.2f", avgWaitingTime));
        JLabel avgTurnaroundLabel = new JLabel(String.format("Average Turnaround Time: %.2f", avgTurnaroundTime));

        statsPanel.add(avgWaitingLabel);
        statsPanel.add(avgTurnaroundLabel);

        // Add panels to the main panel
        mainPanel.add(ganttPanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}