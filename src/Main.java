import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManagerImpl manager = new InMemoryTaskManagerImpl();
        Task task1 = new Task("task1", "des1", TaskStatus.NEW);
        Task task2 = new Task("task2", "des2", TaskStatus.NEW);
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("epic1", "descriptionEpic1", TaskStatus.NEW);
        Epic epic2 = new Epic("epic2", "descriptionEpic2", TaskStatus.NEW);
        final int epicId1 = manager.addNewEpic(epic1);
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, epicId1);
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, epicId1);
        Subtask subtask3 = new Subtask("sub3", "des3", TaskStatus.NEW, epicId1);
        final Integer subtask1Id = manager.addNewSubtask(subtask1);
        final Integer subtask2Id = manager.addNewSubtask(subtask2);
        final Integer subtask3Id = manager.addNewSubtask(subtask3);
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getSubtask(subtask2Id));
        System.out.println(manager.getEpic(epicId2));
        System.out.println();

        System.out.println(manager.getSubtask(subtask2Id));
        System.out.println(manager.getHistory());
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getHistory());
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getHistory());
        System.out.println();

        manager.deleteTask(taskId1);
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getHistory());
        manager.deleteEpic(epicId1);
        System.out.println(manager.getHistory());
    }
}
