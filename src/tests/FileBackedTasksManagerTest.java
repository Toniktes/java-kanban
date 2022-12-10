package tests;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file;
    Path path;

    @BeforeEach
    public void beforeEach() {
        file = new File("resources/task.csv");
        path = Path.of(file.getAbsolutePath());
        manager = new FileBackedTasksManager(file);
        initTasks();
    }

    @AfterEach
    public void afterEach() {
        manager.clearAll();
        task = null;
        epic = null;
        subtask = null;
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void loadFromFailTest() {

        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager(file);
        fileBackedTasksManager2.loadFromFile();
        List<Task> tasks = fileBackedTasksManager2.getTasks();
        Task task1 = new Task(1, "task1", "des1", TaskStatus.NEW, 1, LocalDateTime.of(2022, 12, 1, 10, 30));
        Task task2 = new Task(2, "task2", "des2", TaskStatus.NEW, 1, LocalDateTime.of(2022, 12, 2, 10, 30));
        assertEquals(tasks, List.of(task1, task2), "История задач не считана");

    }

    @Test
    void loadFromFailEpicNoSubtasks() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.loadFromFile();
        fileBackedTasksManager.clearAll();
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getTasks());
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getEpics());
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getSubtasks());
    }

    @Test
    void loadFromFailEmptyHistory() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.loadFromFile();
        fileBackedTasksManager.cleanHistory();
        assertEquals(Collections.EMPTY_LIST, fileBackedTasksManager.getHistory());
    }
}
