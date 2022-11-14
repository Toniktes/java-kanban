package manager;

import tasks.*;

import java.util.Collections;
import java.util.List;

public class CsvTaskFormat {

    public static String toString(Task task) {
        String epicId;

        if (task instanceof Subtask) {
            epicId = String.valueOf((((Subtask) task).getEpicId()));
        } else {
            epicId = "";
        }

        String arrayOfTask[] = {Integer.toString(task.getId()), getType(task).toString(), task.getName(), task.getStatus().toString(), task.getDescription(), epicId};
        return String.join(",", arrayOfTask);
    }

    public static Task fromString(String value) {
        final String[] values = value.split(",");//id,type,name,status,description,epic
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        if (type == TaskType.TASK) {
            return new Task(id, values[2], values[4], TaskStatus.valueOf(values[3]));
        } else if (type == TaskType.EPIC) {
            return new Epic(id, values[2], values[4], TaskStatus.valueOf(values[3]));//int id, String name, String description, TaskStatus status
        } else {
            return new Subtask(id, values[2], values[4], TaskStatus.valueOf(values[3]), Integer.parseInt(values[5]));//int id, String name, String description, TaskStatus status, int epicId
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }
        for (Task task : history) {
            sb = sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> ids = Collections.emptyList();

        if (value != null) {
            final String[] values = value.split(",");
            for (String id : values) {
                ids.add(Integer.parseInt(id));
            }
            return ids;
        }
        return ids;
    }

    public static TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }
}
