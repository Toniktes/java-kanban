package manager;

public class Manager {

    public static TaskManager getDefault() {
        return new InMemoryTaskManagerImpl();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManagerImpl();
    }
}
