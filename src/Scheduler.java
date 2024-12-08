import java.util.List;
import java.util.Map;

public interface Scheduler {
    List<Process> schedule(List<Process> processes);
    public List<Execution> getExecutions();
    Map<String, Double> getSchedulingSummary();
}
