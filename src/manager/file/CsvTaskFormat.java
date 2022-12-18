package manager.file;

import manager.history.HistoryManager;
import manager.TaskType;
import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvTaskFormat {

    public static String toString(Task task) {
        String epicId;

        if (task instanceof Subtask) {
            epicId = String.valueOf((((Subtask) task).getEpicId()));
        } else {
            epicId = "";
        }

        String arrayOfTask[] = {Integer.toString(task.getId()), getType(task).toString(), task.getName(),
                task.getStatus().toString(), task.getDescription(), Long.toString(task.getDuration()), String.valueOf(task.getStartTime()), epicId};
        return String.join(",", arrayOfTask);
    }

    public static Task fromString(String value) {
        final String[] values = value.split(",");//id,type,name,status,description,duration,startTime,epicID
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        if (type == TaskType.TASK) {
            return new Task(id, values[2], values[4], TaskStatus.valueOf(values[3]), Long.parseLong(values[5]), LocalDateTime.parse(values[6]));
        } else if (type == TaskType.EPIC) {//int id, String name, String description, TaskStatus status, long duration, LocalDateTime startTime
            return new Epic(id, values[2], values[4], TaskStatus.valueOf(values[3]), Long.parseLong(values[5]), LocalDateTime.parse(values[6]));
        } else {
            return new Subtask(id, values[2], values[4], TaskStatus.valueOf(values[3]), Long.parseLong(values[5]), LocalDateTime.parse(values[6]), Integer.parseInt(values[7]));
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }
        for (Task task : history) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();

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
