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
    private int createId = 0;
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    @Override
    public List<Task> getTasks() {//получить все таски
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {//получить все эпики
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {//получить все сабтаски
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
        ArrayList<Integer> idSubstaks = new ArrayList<>(getEpic(epicId).getIdSubtasks());
        ArrayList<Subtask> epicSubtask = new ArrayList<>();
        for (Integer idSubstak : idSubstaks) {
            epicSubtask.add(getSubtask(idSubstak));
        }
        return epicSubtask;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = ++createId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++createId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        if (getEpic(subtask.getEpicId()) == null) {
            System.out.println("Такого эпика нет");
            return -1;
        } else {
            final int id = ++createId;
            subtask.setId(id);
            subtasks.put(id, subtask);
            getEpic(subtask.getEpicId()).addIdSubtasks(id);
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
    }

    @Override
    public void deleteEpic(int id) {
        for (int key : getEpic(id).getIdSubtasks()) {
            subtasks.remove(key);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = getSubtask(id);
        if (subtask == null) {
            System.out.println("Не удалось удалить подзадачу с id - " + id + "такой подзадачи не существует");
        } else {
            Epic epic = getEpic(subtask.getEpicId());
            epic.removeSubtask(id);
            subtasks.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
