import manager.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import manager.Managers;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManagerImpl manager = new InMemoryTaskManagerImpl();
        Task task1 = new Task("task1", "des1", TaskStatus.NEW);//создаем 2 таски
        Task task2 = new Task("task1", "des1", TaskStatus.NEW);
        final int taskId1 = manager.addNewTask(task1);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("epic1", "descriptionEpic1", TaskStatus.NEW);//создаем 2 эпика
        Epic epic2 = new Epic("epic2", "descriptionEpic2", TaskStatus.NEW);
        final int epicId1 = manager.addNewEpic(epic1);//добавляем их в мапу и возвращаем id
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, epicId1);//создаем сабтаски
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, epicId1);//2е в 1й эпик и 1а во 2й
        Subtask subtask3 = new Subtask("sub3", "des3", TaskStatus.NEW, epicId2);
        final Integer subtask1Id = manager.addNewSubtask(subtask1);//добавляем в мапу и возвращаем id
        final Integer subtask2Id = manager.addNewSubtask(subtask2);
        final Integer subtask3Id = manager.addNewSubtask(subtask3);
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println();

        manager.updateTask(task1 = new Task(task1.getId(), "task1", "des1", TaskStatus.IN_PROGRESS));//апдтейтим таски и сабтаски
        manager.updateTask(task2 = new Task(task2.getId(), "task1", "des1", TaskStatus.DONE));
        manager.updateSubtask(subtask1 = new Subtask(subtask1.getId(), "sub1", "des1", TaskStatus.IN_PROGRESS, epicId1));
        manager.updateSubtask(subtask2 = new Subtask(subtask2.getId(), "sub2", "des2", TaskStatus.DONE, epicId1));
        manager.updateSubtask(subtask3 = new Subtask(subtask3.getId(), "sub3", "des3", TaskStatus.DONE, epicId2));
        System.out.println(manager.getTasks());//проверяем
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getTask(2));
        System.out.println(manager.getHistory());
        System.out.println();

        manager.deleteTask(task1.getId());//удаляем таску, сабтаску и эпик
        manager.deleteSubtask(subtask3.getId());
        manager.deleteEpic(epic1.getId());
        System.out.println(manager.getTasks());//проверяем
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtask(subtask1Id));
        System.out.println(manager.getHistory());


    }
}
