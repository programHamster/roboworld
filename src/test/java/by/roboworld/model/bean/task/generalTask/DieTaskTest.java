package by.roboworld.model.bean.task.generalTask;

import by.roboworld.model.bean.task.Task;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DieTaskTest {

    @Test
    public void perform() {
        // arrange
        Task task = new DieTask();
        Thread currentThread = Thread.currentThread();
        // act
        task.perform();
        // assert
        assertTrue(currentThread.isInterrupted());
    }
}