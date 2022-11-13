package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManagerImpl implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int createdId = 0;
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        List<Integer> idSubtasks = new ArrayList<>(epics.get(epicId).getIdSubtasks());
        List<Subtask> epicSubtask = new ArrayList<>();
        for (Integer idSubtask : idSubtasks) {
            epicSubtask.add(subtasks.get(idSubtask));
        }
        return epicSubtask;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = ++createdId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++createdId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        if (epics.get(subtask.getEpicId()) == null) {
            System.out.println("Такого эпика нет");
            return -1;
        } else {
            final int id = ++createdId;
            subtask.setId(id);
            subtasks.put(id, subtask);
            epics.get(subtask.getEpicId()).addIdSubtasks(id);
            updateEpicStatus(subtask.getEpicId());
            return id;
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> sub = epic.getIdSubtasks();
        if (subtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        TaskStatus status = TaskStatus.NEW;
        int index = 0;

        for (int id : sub) {
            Subtask subtask = subtasks.get(id);
            if (subtask.getStatus().equals(TaskStatus.NEW)) {
                continue;
            }
            if (subtask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                status = TaskStatus.IN_PROGRESS;
                continue;
            }
            if (subtask.getStatus().equals(TaskStatus.DONE)) {
                index++;
            }
        }
        if (index == sub.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(status);
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        for (int idSubtask : epics.get(id).getIdSubtasks()) {
            subtasks.remove(idSubtask);
            historyManager.remove(idSubtask);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Не удалось удалить подзадачу с id - " + id + "такой подзадачи не существует");
        } else {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addToHistory(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        } else if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        } else if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
        }
    }
}
