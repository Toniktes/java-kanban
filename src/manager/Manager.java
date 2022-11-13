package manager;

import java.io.File;

public class Manager {

    public static TaskManager getDefault() {
        return new InMemoryTaskManagerImpl();
    }

    public static TaskManager getManagerForCsv() {
        return new FileBackedTasksManager(new File("resources/task.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManagerImpl();
    }

}
