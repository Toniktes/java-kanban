package tests;

import manager.NoValueFoundException;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected void initTasks() {
        Task task1 = new Task("task1", "des1", TaskStatus.NEW, 1, LocalDateTime.of(2022, 12, 1, 10, 30));
        Task task2 = new Task("task2", "des2", TaskStatus.NEW, 1, LocalDateTime.of(2022, 12, 2, 10, 30));
        final int taskId1 = manager.addNewTask(task1);//1
        final int taskId2 = manager.addNewTask(task2);//2
        Epic epic1 = new Epic("epic1", "descriptionEpic1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 10, 30));
        Epic epic2 = new Epic("epic2", "descriptionEpic2", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 4, 10, 30));
        final int epicId1 = manager.addNewEpic(epic1);//3
        final int epicId2 = manager.addNewEpic(epic2);//4
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 10, 30), epicId1);
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 3, 15, 30), epicId1);
        Subtask subtask3 = new Subtask("sub3", "des3", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 4, 17, 30), epicId2);
        final Integer subtask1Id = manager.addNewSubtask(subtask1);//5
        final Integer subtask2Id = manager.addNewSubtask(subtask2);//6
        final Integer subtask3Id = manager.addNewSubtask(subtask3);//7
    }

    @Test
    void getTasksNotNull() {
        final List<Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Возвращает пустой список задач Task");
    }

    @Test
    void getEpicsNotNull() {
        final List<Epic> epics = manager.getEpics();
        assertNotNull(epics, "Возвращает пустой список задач Epic");
    }

    @Test
    void getSubtasksNotNull() {
        final List<Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Возвращает пустой список задач Subtask");
    }

    @Test
    void getEpicSubtasksNotNull() {
        List<Subtask> subtasks = manager.getEpicSubtasks(3);
        assertNotNull(subtasks, "Возвращает пустой список подзадач Эпика");
    }

    @Test
    void clearAllReturnEmptyLists() {
        manager.clearAll();
        assertTrue(manager.getTasks().isEmpty(), "Таски не удалены");
        assertTrue(manager.getEpics().isEmpty(), "Эпики не удалены");
        assertTrue(manager.getEpics().isEmpty(), "Сабтаски не удалены");
    }

    @Test
    void getTaskId1() {
        int id = manager.getTask(1).getId();
        assertEquals(1, id);
    }

    @Test
    void getTaskNoValueFoundException() {
        final NoValueFoundException exception = assertThrows(
                NoValueFoundException.class,
                () -> manager.getTask(111));
        assertEquals("Нет таски с таким id", exception.getMessage());
    }

    @Test
    void getEpicId3() {
        int id = manager.getEpic(3).getId();
        assertEquals(3, id);
    }

    @Test
    void getEpicNoValueFoundException() {
        final NoValueFoundException exception = assertThrows(
                NoValueFoundException.class,
                () -> manager.getEpic(111));
        assertEquals("Нет эпика с таким id", exception.getMessage());
    }

    @Test
    void getSubtaskId5() {
        int id = manager.getSubtask(5).getId();
        assertEquals(5, id);
        int idEpic = manager.getSubtask(5).getEpicId();
        assertNotEquals(0, idEpic, "Сабтаска без эпика!");
    }

    @Test
    void getSubtaskNoValueFoundException() {
        final NoValueFoundException exception = assertThrows(
                NoValueFoundException.class,
                () -> manager.getSubtask(111));
        assertEquals("Нет сабтаски с таким id", exception.getMessage());

    }

    @Test
    void addNewTaskTest() {
        task = new Task("task8", "des8", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 8, 10, 30));//id=8
        int id = manager.addNewTask(task);
        assertEquals(id, task.getId());
    }

    @Test
    void addNewEpicTestAndCheckStatus() {
        epic = new Epic("epic8", "descriptionEpic8", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 8, 10, 30));//id=8
        int id = manager.addNewEpic(epic);
        assertEquals(id, epic.getId());
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void addNewSubtaskAndCheckHaveEpic() {
        subtask = new Subtask("sub8", "des8", TaskStatus.NEW, 120, LocalDateTime.of(2022, 12, 8, 10, 30), 4);
        int id = manager.addNewSubtask(subtask);
        assertEquals(id, subtask.getId());
        assertEquals(4, subtask.getEpicId());
        assertNotNull(manager.getEpic(subtask.getEpicId()));
    }

    @Test
    void updateTaskChangeStatusInProgress() {
        task = new Task(1, "task1", "des1", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2022, 12, 1, 10, 30));
        manager.updateTask(task);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getTask(1).getStatus());
    }

    @Test
    void updateSubtaskChangeInProgressAndCheckHaveEpic() {
        subtask = new Subtask(5, "sub1", "des1", TaskStatus.IN_PROGRESS, 120, LocalDateTime.of(2022, 12, 1, 10, 30), 3);
        manager.updateSubtask(subtask);
        assertEquals(TaskStatus.IN_PROGRESS, manager.getSubtask(5).getStatus());
        assertEquals(3, subtask.getEpicId());
        assertNotNull(manager.getEpic(subtask.getEpicId()));
    }

    @Test
    void deleteTaskId1AndCheck() {
        manager.deleteTask(1);
        final NoValueFoundException exception = assertThrows(
                NoValueFoundException.class,
                () -> manager.getTask(1));
        assertEquals("Нет таски с таким id", exception.getMessage());
    }

    @Test
    void deleteEpicId3() {
        manager.deleteEpic(3);
        final NoValueFoundException exception = assertThrows(
                NoValueFoundException.class,
                () -> manager.getEpic(3));
        assertEquals("Нет эпика с таким id", exception.getMessage());
    }

    @Test
    void deleteSubtaskId5() {
        manager.deleteSubtask(5);
        final NoValueFoundException exception = assertThrows(
                NoValueFoundException.class,
                () -> manager.getSubtask(5));
        assertEquals("Нет сабтаски с таким id", exception.getMessage());
    }

    @Test
    void getHistorySize2() {
        manager.getTask(1);
        manager.getEpic(4);
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());

    }

    @Test
    void cleanHistoryReturnEmpty() {
        manager.getTask(2);
        manager.getEpic(3);
        manager.getSubtask(6);
        manager.cleanHistory();
        List<Task> history = manager.getHistory();
        assertEquals(0, history.size());
    }
}
