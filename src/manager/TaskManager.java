package manager;

import manager.history.HistoryManager;
import tasks.*;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Subtask> getEpicSubtasks(int epicId);

    void clearAll();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateEpicStatus(int epicId);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void deleteAllEpics();

    void deleteAllSubtasks();

    List<Task> getHistory();

    HistoryManager getHistoryManager();

    void addToHistory(int id);
    void cleanHistory();

    void deleteAllTasks();

    List<Task> getPrioritizedTasks();
}
