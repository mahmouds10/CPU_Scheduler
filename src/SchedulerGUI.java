import javax.swing.*;
import java.awt.*;
import java.util.List;

class SchedulerGUI {
    public SchedulerGUI(String schedulerName, List<Execution> executions, double avgWaitingTime, double avgTurnaroundTime) {
        JFrame frame = new JFrame(schedulerName + " Scheduler Visualization");
        frame.setSize(1200, 700); // Increased overall frame width
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Calculate total time span
        int minStartTime = Integer.MAX_VALUE;
        int maxEndTime = Integer.MIN_VALUE;

        for (Execution execution : executions) {
            minStartTime = Math.min(minStartTime, execution.startTime);
            maxEndTime = Math.max(maxEndTime, execution.endTime);
        }

        int totalDuration = maxEndTime - minStartTime;
        int timeUnitWidth = 60; // Scale for visualization
        int ganttChartWidth = timeUnitWidth * totalDuration;

        // Gantt Chart Panel
        JPanel ganttPanel = new JPanel();
        ganttPanel.setLayout(null);
        ganttPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        ganttPanel.setPreferredSize(new Dimension(ganttChartWidth, 150)); // Adjust width based on the total duration
        ganttPanel.setBackground(Color.WHITE);

        int currentX = 10;

        for (Execution execution : executions) {
            int width = timeUnitWidth * (execution.endTime - execution.startTime);

            // Create a block for execution
            JPanel block = new JPanel();
            block.setBounds(currentX, 30, width, 100);
            block.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Set the background color based on the execution's color property
            Color blockColor;
            try {
                blockColor = Color.decode(execution.color); // Decode string to Color object
            } catch (IllegalArgumentException e) {
                blockColor = Color.LIGHT_GRAY; // Default to LIGHT_GRAY if the color is invalid
            }
            block.setBackground(blockColor);

            ganttPanel.add(block);

            // Add process name inside the block
            JLabel nameLabel = new JLabel(execution.processName);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            block.add(nameLabel);

            // Add the time label for start time
            JLabel startTimeLabel = new JLabel(String.valueOf(execution.startTime));
            startTimeLabel.setBounds(currentX, 140, 50, 20);
            ganttPanel.add(startTimeLabel);

            currentX += width;
        }

        // Add the end time label for the last process
        JLabel endTimeLabel = new JLabel(String.valueOf(maxEndTime));
        endTimeLabel.setBounds(currentX, 140, 50, 20); // Position after the last execution block
        ganttPanel.add(endTimeLabel);

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.add(new JLabel("Average Waiting Time: " + String.format("%.2f", avgWaitingTime)));
        statsPanel.add(new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTurnaroundTime)));

        mainPanel.add(ganttPanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
