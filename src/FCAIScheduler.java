import java.util.List;
import java.util.Map;

public class FCAIScheduler implements Scheduler {
    @Override
    public void schedule(List<Process> processes) {

    }

    @Override
    public List<Execution> getExecutions() {
        return List.of();
    }

    @Override
    public Map<String, Double> getSchedulingSummary() {
        return Map.of();
    }
}
