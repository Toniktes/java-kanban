package tests;

import manager.HistoryManager;
import manager.InMemoryHistoryManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerImplTest {
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    HistoryManager historyManager;


    Task task1 = new Task(1, "task1", "des1", TaskStatus.NEW, 0, LocalDateTime.now());
    Task task2 = new Task(2, "task2", "des2", TaskStatus.NEW, 0, LocalDateTime.now());

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManagerImpl();
    }

    @AfterEach
    void afterEach() {
        historyManager.cleanHistory();
    }

    @Test
    void addTwoTasksToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }

    @Test
    void returnEmptyHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        historyManager.remove(task2.getId());
        assertEquals(Collections.EMPTY_LIST, historyManager.getHistory());
    }

    @Test
    void deleteOneTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        assertEquals(List.of(task2), historyManager.getHistory());
    }

    @Test
    void removeWrongId() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(4);
        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }
}
