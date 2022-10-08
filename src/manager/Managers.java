package manager;

public class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManagerImpl();
    }

    public static HistoryManager getDefaultHistory() {
        return new inMemoryHistoryManagerImpl();
    }
}
