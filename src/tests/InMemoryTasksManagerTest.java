package tests;

import manager.InMemoryTaskManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManagerImpl> {

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManagerImpl();
        initTasks();
    }

    @AfterEach
    public void afterEach() {
        manager.clearAll();
        task = null;
        epic = null;
        subtask = null;
    }
}
