package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.adapters.LocalDateTimeAdapter;
import manager.file.FileBackedTasksManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManagerImpl;
import manager.http.HttpTaskManager;
import server.KVServer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

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

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();

    }

    public static HttpTaskManager getDefault(HistoryManager historyManager) throws IOException, InterruptedException {
        return new HttpTaskManager(historyManager, "http://localhost:" + KVServer.PORT);
    }

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager();
    }
}
