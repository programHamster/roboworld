package by.roboworld.model.bean.task;

import by.roboworld.model.bean.task.generalTask.DieTask;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import by.roboworld.model.facroty.Constants;
import by.roboworld.model.facroty.RobotType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class TaskHolderTest {
    private static final TaskHolder HOLDER = TaskHolder.getInstance();

    @Test
    public void getInstance_checkConcurrent() throws InterruptedException {
        // arrange
        final int NUMBER_THREADS = 5;
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
        List<Callable<TaskHolder>> callableHolders = new ArrayList<>(NUMBER_THREADS);
        final boolean[] result = new boolean[1];
        result[0] = true;
        for(int i=0; i<NUMBER_THREADS; i++){
            callableHolders.add(TaskHolder::getInstance);
        }
        // act
        List<Future<TaskHolder>> futures = executor.invokeAll(callableHolders);
        futures.stream().map(future -> {
            TaskHolder holder = null;
            try {
                holder = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return holder;
        }).reduce((holder1, holder2) -> {
            if(holder1 == null || holder2 == null || holder1 != holder2){
                result[0] = false;
            }
            return holder2;
        });
        // assert
        assertTrue(result[0]);
    }

    @Test
    public void testGetTaskAndPutTask() {
        // arrange
        Task backEndTask = new BackEndTask();
        Task frontEndTask = new FrontEndTask();
        Task hrTask = new HRTask();
        Task dieTask = new DieTask();
        synchronized (HOLDER){
            // act
            HOLDER.putTask(backEndTask);
            HOLDER.putTask(frontEndTask);
            HOLDER.putTask(hrTask);
            HOLDER.putTask(dieTask);
            // assert
            assertEquals(backEndTask, HOLDER.getTask(backEndTask.getName(), RobotType.BACK_END_DEVELOPER));
            assertEquals(frontEndTask, HOLDER.getTask(frontEndTask.getName(), RobotType.FRONT_END_DEVELOPER));
            assertEquals(hrTask, HOLDER.getTask(hrTask.getName(), RobotType.HR));
            assertEquals(dieTask, HOLDER.getTask(dieTask.getName(), RobotType.GENERAL));
            assertEquals(dieTask, HOLDER.getTask(dieTask.getName(), RobotType.BACK_END_DEVELOPER));
            assertNotEquals(backEndTask, HOLDER.getTask(dieTask.getName(), RobotType.BACK_END_DEVELOPER));
            assertNull(HOLDER.getTask(frontEndTask.getName(), RobotType.HR));
        }
    }

    @Test(expected = NullPointerException.class)
    public void get_taskNameIsNull() {
        // arrange
        HOLDER.getTask(null, RobotType.GENERAL);
    }

    @Test(expected = NullPointerException.class)
    public void get_typeIsNull() {
        // arrange
        HOLDER.getTask(Constants.SOME_NAME, null);
    }

    @Test
    public void totalSearch() {
        // arrange
        Task task = new BackEndTask();
        Task result;
        // act
        synchronized (HOLDER){
            HOLDER.putTask(task);
            result = HOLDER.totalSearch(task.getName());
        }
        // assert
        assertEquals(task, result);
    }
}