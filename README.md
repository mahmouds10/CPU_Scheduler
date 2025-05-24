# CPU Scheduling Simulator

A Java-based application for simulating various CPU scheduling algorithms including:
- **First-Come, First-Served (FCFS)**
- **Shortest Job First (SJF)**
- **Shortest Remaining Time First (SRTF)**
- **Priority Scheduling**
- **Custom FCAI Scheduler**

## Features

- Simulates different scheduling strategies
- GUI support for visual feedback
- Logging of detailed execution stats
- Custom input via file or console
- Calculation of performance metrics like waiting time, turnaround time, and throughput

## Class Structure Overview

### Main Components

- **CPUSchedulersSimulator**: Entry point and main coordinator for scheduling strategies.
- **Scheduler (Interface)**: Abstract representation of all scheduling algorithms.
  - `SJF`, `SRTF`, `PriorityScheduler`: Implement standard algorithms.
  - `FCAIScheduler`: A custom flexible strategy.
- **Process & ProcessThread**: Represents processes (and threads for preemptive behavior).
- **Execution**: Captures the runtime behavior and statistics of a process.
- **Logger**: Utility class for logging states and transitions.
- **ResultsCalculator**: Aggregates and reports on scheduler results.
- **SchedulerGUI**: GUI to visualize the schedule and stats.

## How to Run

1. Compile the project:
   ```bash
   javac -d bin src/**/*.java
   ```

2. Run the simulator:
   ```bash
   java -cp bin CPUSchedulersSimulator
   ```

3. Optionally use GUI or provide input via file using `InputReader`.

## Sample Input

Processes should be defined with:
- name
- arrival time
- burst time
- priority (if applicable)
- quantum (if applicable for FCAI)

## Output

The simulation outputs:
- Execution timeline
- Gantt chart (GUI)
- Statistics like average waiting and turnaround time

## Authors

- Developed for academic simulation of CPU scheduling algorithms.

## License

This project is open-source and free to use under the MIT License.
