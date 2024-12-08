import java.util.List;
import java.util.Map;

public interface Scheduler {
    void schedule(List<Process> processes);
    List<Execution> getExecutions();
    Map<String, Double> getSchedulingSummary();
}
