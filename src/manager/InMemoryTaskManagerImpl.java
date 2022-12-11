package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class InMemoryTaskManagerImpl implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
    protected final Set<Task> prioritizedTasks = new TreeSet<>(comparator);
    protected int createdId = 0;
    protected HistoryManager historyManager = Manager.getDefaultHistory();

    public InMemoryTaskManagerImpl(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManagerImpl() {
    }

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
        cleanHistory();
        prioritizedTasks.clear();
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
        if (task == null) {
            throw new NoValueFoundException("Нет таски с таким id");
        }
        historyManager.add(task);
        return task;

    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NoValueFoundException("Нет эпика с таким id");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            throw new NoValueFoundException("Нет сабтаски с таким id");
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = ++createdId;
        task.setId(id);
        addToPrioritizedTasks(task);
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
            addToPrioritizedTasks(subtask);
            subtasks.put(id, subtask);
            epics.get(subtask.getEpicId()).addIdSubtasks(id);
            updateEpicStatus(subtask.getEpicId());
            return id;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            addToPrioritizedTasks(task);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такой таски нет");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic.getId());
            updateEpicTime(epic);
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    public void updateEpicTime(Epic epic) {
        List<Subtask> subtasks = getEpicSubtasks(epic.getId());
        LocalDateTime startTime = subtasks.get(0).getStartTime();
        LocalDateTime endTime = subtasks.get(0).getEndTime();

        for (Subtask sub : subtasks) {
            if (sub.getStartTime().isBefore(startTime)) {
                startTime = sub.getStartTime();
            }
            if (sub.getEndTime().isAfter(endTime)) {
                endTime = sub.getEndTime();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        Duration dur = Duration.between(startTime, endTime);
        long duration = (endTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() - startTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
        epic.setDuration(dur.toMinutes());
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
            if (subtask.getStatus() == TaskStatus.NEW) {
                continue;
            }
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                status = TaskStatus.IN_PROGRESS;
                continue;
            }
            if (subtask.getStatus() == TaskStatus.DONE) {
                status = TaskStatus.IN_PROGRESS;
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
        if(subtask != null && subtasks.containsKey(subtask.getId())) {
            addToPrioritizedTasks(subtask);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
            updateEpicTime(epics.get(subtask.getEpicId()));
        }
        System.out.println("Такой подзадачи нет");
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        }
        System.out.println("Такой таски нет");
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            for (int idSubtask : epics.get(id).getIdSubtasks()) {
                prioritizedTasks.remove(subtasks.get(idSubtask));
                subtasks.remove(idSubtask);
                historyManager.remove(idSubtask);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Не удалось удалить подзадачу с id - " + id + "такой подзадачи не существует");
        } else {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
            prioritizedTasks.remove(subtask);
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

    @Override
    public void cleanHistory() {
        historyManager.clearHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void addToPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
        checkIntersections();
    }

    public boolean checkTime(Task task) {
        List<Task> tasks = getPrioritizedTasks();
        int sizeTime = 0;
        if (tasks.size() > 0) {
            for (Task newTask : tasks) {
                if (newTask.getStartTime() != null && newTask.getEndTime() != null) {
                    if (task.getStartTime().isBefore(newTask.getStartTime()) && task.getEndTime().isBefore(newTask.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(newTask.getEndTime()) && task.getEndTime().isAfter(newTask.getEndTime())) {
                        return false;
                    }
                } else {
                    sizeTime++;
                }
            }
            return sizeTime == tasks.size();
        } else {
            return true;
        }
    }

    public void checkIntersections() {
        List<Task> tasks = getPrioritizedTasks();
        for (int i = 1; i < tasks.size(); i++) {
            boolean intersection = checkTime(tasks.get(i));

            if (intersection) {
                throw new CheckIntersectionException("Задачи " + tasks.get(i).getId() + " и " + tasks.get(i - 1).getId() + " пересекаются");
            }
        }
    }


}

