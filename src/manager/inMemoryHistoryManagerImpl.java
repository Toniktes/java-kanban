package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class inMemoryHistoryManagerImpl implements HistoryManager {
    List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addTask(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }
}
