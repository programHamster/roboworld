package org.jazzteam.roboworld.model.bean.task;

import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.exception.notSpecified.TaskNameNotSpecifiedException;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.model.facroty.Constants;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class TaskHolderTest {
    private static final TaskHolder HOLDER = TaskHolder.getInstance();

    @Test
    public void getInstance_checkConcurrent() throws InterruptedException, ExecutionException  {
        // arrange
        CompletableFuture<TaskHolder> firstFuture = CompletableFuture.supplyAsync(TaskHolder::getInstance);
        CompletableFuture<TaskHolder> secondFuture = CompletableFuture.supplyAsync(TaskHolder::getInstance);
        // act
        TaskHolder firstResult = firstFuture.get();
        TaskHolder secondResult = secondFuture.get();
        // assert
        assertNotNull(firstResult);
        assertNotNull(secondResult);
        assertTrue(firstResult == secondResult);
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

    @Test(expected = TaskNameNotSpecifiedException.class)
    public void get_taskNameIsNull() {
        // arrange
        HOLDER.getTask(null, RobotType.GENERAL);
    }

    @Test(expected = RobotTypeNotSpecifiedException.class)
    public void get_typeIsNull() {
        // arrange
        HOLDER.getTask(Constants.SOME_NAME, null);
    }

}