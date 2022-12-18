package tests.manager;

import manager.Manager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;

public class EpicTest {
    private TaskManager manager;

    @BeforeEach
    public void beforEach() {
        manager = Manager.getDefaultManager();
        Epic epic1 = new Epic("epic1", "descriptionEpic1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 1, 10, 30));
        manager.addNewEpic(epic1);
    }

    @AfterEach
    public void afterEach() {
        manager.clearAll();
    }

    public void addSubtasks() {
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 2, 10, 30), 1);
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 10, 30), 1);
        final Integer subtask1Id = manager.addNewSubtask(subtask1);
        final Integer subtask2Id = manager.addNewSubtask(subtask2);
    }

    @Test
    public void statusOnEmptyEpicIsNew() {
        TaskStatus statusOfEpic = manager.getEpic(1).getStatus();
        Assertions.assertEquals(TaskStatus.NEW, statusOfEpic);
    }

    @Test
    public void statusOfEpicWithSubtasksIsNew() {
        addSubtasks();
        TaskStatus statusOfEpic = manager.getEpic(1).getStatus();
        Assertions.assertEquals(TaskStatus.NEW, statusOfEpic);
    }

    @Test
    public void statusOfEpicWithSubtasksIsDone() {
        addSubtasks();
        manager.getSubtask(2).setStatus(TaskStatus.DONE);
        manager.getSubtask(3).setStatus(TaskStatus.DONE);
        manager.updateEpicStatus(1);
        TaskStatus statusOfEpic = manager.getEpic(1).getStatus();
        Assertions.assertEquals(TaskStatus.DONE, statusOfEpic);
    }

    @Test
    public void statusOfEpicWithSubtasksIsDoneAndNew() {
        addSubtasks();
        manager.getSubtask(2).setStatus(TaskStatus.DONE);
        manager.getSubtask(3).setStatus(TaskStatus.NEW);
        manager.updateEpicStatus(1);
        TaskStatus statusOfEpic = manager.getEpic(1).getStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, statusOfEpic);
    }

    @Test
    public void statusOfEpicWithSubtasksInProgress() {
        addSubtasks();
        manager.getSubtask(2).setStatus(TaskStatus.IN_PROGRESS);
        manager.getSubtask(3).setStatus(TaskStatus.IN_PROGRESS);
        manager.updateEpicStatus(1);
        TaskStatus statusOfEpic = manager.getEpic(1).getStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, statusOfEpic);
    }
}
