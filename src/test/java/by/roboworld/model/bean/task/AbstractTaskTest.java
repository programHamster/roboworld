package by.roboworld.model.bean.task;

import by.roboworld.model.bean.task.specialTask.BackEndTask;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

public class AbstractTaskTest {

    @Test
    public void getId_concurrentTestId() throws InterruptedException {
        // arrange
        int numberThreads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(numberThreads);
        List<Callable<Task>> callables = new ArrayList<>(numberThreads);
        for(int i=0; i<numberThreads; i++){
            callables.add(BackEndTask::new);
        }
        // act
        List<Future<Task>> futures = executor.invokeAll(callables);
        int[] taskIds = futures.parallelStream().mapToInt(future -> {
            try {
                return future.get().getId();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }).sequential().sorted().toArray();
        executor.shutdown();
        System.out.println("Check the task id sequential : " + Arrays.toString(taskIds));
        boolean result = true;
        for(int i=1; i<taskIds.length; i++){
            int delta = taskIds[i] - taskIds[i - 1];
            if(delta != 1){
                result = false;
                break;
            }
        }
        // assert
        assertTrue(result);
    }
}