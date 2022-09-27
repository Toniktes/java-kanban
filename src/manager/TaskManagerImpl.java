package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManagerImpl implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int createId = 0;//Идентификатор задачи

    @Override
    public List<Task> getTasks() {//получить все таски
        ArrayList<Task> task = new ArrayList<>(tasks.values());
        return task;
    }

    @Override
    public List<Epic> getEpics() {//получить все эпики
        ArrayList<Epic> epic = new ArrayList<>(epics.values());
        return epic;
    }

    @Override
    public List<Subtask> getSubtasks() {//получить все сабтаски
        ArrayList<Subtask> subtask = new ArrayList<>(subtasks.values());
        return subtask;
    }

    @Override
    public void clearAll() {//удалить все задачи
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {//полчуить все сбатаски эпика
        ArrayList<Integer> idSubstaks = new ArrayList<>(getEpic(epicId).getIdSubtasks());//получаем лист id сабов в эпике
        ArrayList<Subtask> epicSubtask = new ArrayList<>();
        for (int i = 0; i < idSubstaks.size(); i++) {//получаем все сабы из эпика
            epicSubtask.add(getSubtask(idSubstaks.get(i)));//добавляем их в лист
        }
        return epicSubtask;
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
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
            getEpic(subtask.getEpicId()).addIdSubtasks(id);//добавлем саб в нужный эпик
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
        List<Integer> sub = epic.getIdSubtasks();//полчаем id сабов в эпике
        if (subtasks.isEmpty()) {
            epic.setStatus("NEW");//если эпик без сабов, то new
            return;
        }
        String status = "NEW";
        int index = 0;

        for (int id : sub) {
            Subtask subtask = subtasks.get(id);//находим сабы, которые входят в эпик
            if (subtask.getStatus().equals("NEW")) {//если new то статус не меняем
                continue;
            }
            if (subtask.getStatus().equals("IN_PROGRESS")) {//если есть хоть один в процессе, значит меняем статус
                status = "IN_PROGRESS";
                continue;
            }
            if (subtask.getStatus().equals("DONE")) {//считаем количество выполненных сабов
                index++;
            }
        }
        if (index == sub.size()) {//если совпадает кол-во done и всего сабов, то статус done
            epic.setStatus("DONE");
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
        Subtask subtask = getSubtask(id);//полчаем искомую сабтаску
        if (subtask == null) {
            System.out.println("Не удалось удалить подзадачу с id - " + id + "такой подзадачи не существует");
        } else {
            Epic epic = getEpic(subtask.getEpicId());//получаем эпик этой сабтаски
            epic.removeSubtask(id);//удаляем id сабтаски из эпика
            subtasks.remove(id);//удаляем сабтаску
        }
    }
}
