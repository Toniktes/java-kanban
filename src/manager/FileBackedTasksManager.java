package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManagerImpl {
    private final File file;
    private final String heading = "id,type,name,status,description,epic";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("")) {
                    break;
                }
                Task task = CsvTaskFormat.fromString(line);

                if (task instanceof Epic) {
                    taskManager.addNewEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    taskManager.addNewSubtask((Subtask) task);
                } else {
                    taskManager.addNewTask(task);
                }
            }

            String lineHistory = br.readLine();
            for (int id : CsvTaskFormat.historyFromString(lineHistory)) {
                taskManager.addToHistory(id);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("ошибка чтения из файла");
        }
        return taskManager;
    }

    protected void save() {
        try {
            if (!Files.deleteIfExists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Такого файла для записи нет");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(heading);

            for (Task task : getTasks()) {
                writer.write(CsvTaskFormat.toString(task) + "/n");
            }
            for (Epic epic : getEpics()) {
                writer.write(CsvTaskFormat.toString(epic) + "/n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CsvTaskFormat.toString(subtask) + "/n");
            }
            writer.write("/n");

            writer.write(CsvTaskFormat.historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("ошибка сохранения в файл");
        }
    }


    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        final int id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

}


