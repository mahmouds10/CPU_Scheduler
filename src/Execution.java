public class Execution {
    String processName;
    String state;
    int startTime;
    int endTime;
    String color;

    public Execution(String processName, String state, int startTime, int endTime, String color) {
        this.processName = processName;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("Process %s %s at time %d%s",
                processName,
                state,
                startTime,
                state.equals("executes") ? " to " + endTime : "");
    }
}
