package manager;

import java.io.File;

public class Manager {

    public static TaskManager getDefaultManager() {
        return new InMemoryTaskManagerImpl();
    }

    public static TaskManager InMemoryTaskManagerImpl(HistoryManager historyManager) {
        return new InMemoryTaskManagerImpl(historyManager);
    }

    public static FileBackedTasksManager getManagerForCsv(File file) {
        return new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManagerImpl();
    }

}
