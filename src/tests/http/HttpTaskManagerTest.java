package tests.http;

import manager.*;
import manager.history.HistoryManager;
import manager.http.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import tests.manager.TaskManagerTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest<T extends TaskManagerTest<HttpTaskManager>> {
    private KVServer server;
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Manager.getDefaultHistory();
            manager = Manager.getDefault(historyManager);
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldLoadTasks() {
        Task task1 = new Task("task1", "des1", TaskStatus.NEW, 1, LocalDateTime.of(2022, 12, 1, 10, 30));
        Task task2 = new Task("task2", "des2", TaskStatus.NEW, 1, LocalDateTime.of(2022, 12, 2, 10, 30));
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getTasks(), list);
    }

    @Test
    public void shouldLoadEpics() {
        Epic epic1 = new Epic("epic1", "descriptionEpic1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 10, 30));
        Epic epic2 = new Epic("epic2", "descriptionEpic2", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 4, 10, 30));
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getEpics(), list);
    }

    @Test
    public void shouldLoadSubtasks() {
        Epic epic1 = new Epic("epic1", "descriptionEpic1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 10, 30));
        manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 10, 30), epic1.getId());
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 15, 30), epic1.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        List<Task> list = manager.getHistory();
        assertEquals(manager.getSubtasks(), list);
    }
}

