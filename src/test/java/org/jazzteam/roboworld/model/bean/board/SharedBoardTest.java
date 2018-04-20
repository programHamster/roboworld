package org.jazzteam.roboworld.model.bean.board;

import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class SharedBoardTest {
    private static final SharedBoard BOARD = SharedBoard.getInstance();

    @Test
    public void getInstance() throws InterruptedException, ExecutionException {
        // arrange
        CompletableFuture<SharedBoard> firstFuture = CompletableFuture.supplyAsync(SharedBoard::getInstance);
        CompletableFuture<SharedBoard> secondFuture = CompletableFuture.supplyAsync(SharedBoard::getInstance);
        // act
        SharedBoard firstResult = firstFuture.get();
        SharedBoard secondResult = secondFuture.get();
        // assert
        assertNotNull(firstResult);
        assertNotNull(secondResult);
        assertTrue(firstResult == secondResult);
    }

    @Test
    public void put() {
        // arrange
        Task generalTask = new DieTask();
        Task backEndTask = new BackEndTask();
        Task frontEndTask = new FrontEndTask();
        Task hrTask = new HRTask();
        // act
        synchronized (BOARD){
            BOARD.put(generalTask);
            BOARD.put(backEndTask);
            BOARD.put(frontEndTask);
            BOARD.put(hrTask);
            // assert
            assertEquals(generalTask, BOARD.get(RobotType.GENERAL));
            assertEquals(backEndTask, BOARD.poll(RobotType.BACK_END_DEVELOPER));
            assertEquals(frontEndTask, BOARD.get(RobotType.FRONT_END_DEVELOPER));
            assertEquals(hrTask, BOARD.get(RobotType.HR));
            // because the back end developer haven't tasks
            assertEquals(generalTask, BOARD.get(RobotType.BACK_END_DEVELOPER));
            assertNotEquals(hrTask, BOARD.get(RobotType.FRONT_END_DEVELOPER));
        }
    }

    @Test(expected = TaskIsNullException.class)
    public void put_null() {
        // act
        SharedBoard.getInstance().put(null);
    }

    @Test
    public void poll() {
        // arrange
        int numberBefore;
        int numberAfter;
        Task hrTask = new HRTask();
        // act
        synchronized (BOARD){
            numberBefore = BOARD.numberTasks(RobotType.GENERAL);
            BOARD.put(hrTask);
            BOARD.poll(RobotType.HR);
            numberAfter = BOARD.numberTasks(RobotType.GENERAL);
        }
        // assert
        assertEquals(numberBefore, numberAfter);
    }

    @Test(expected = NullPointerException.class)
    public void poll_null() {
        SharedBoard.getInstance().poll(null);
    }

    @Test
    public void numberTasks() {
        // arrange
        int[] numberBefore;
        int[] numberAfter;
        synchronized (BOARD){
            numberBefore = countNumberTasks();
            BOARD.put(new DieTask());
            BOARD.put(new BackEndTask());
            BOARD.put(new FrontEndTask());
            BOARD.put(new HRTask());
            numberAfter = countNumberTasks();
        }
        // assert
        for(int i=0; i<numberBefore.length; i++){
            assertEquals(numberBefore[i] + 1, numberAfter[i]);
        }
    }

    private int[] countNumberTasks(){
        int[] number = new int[RobotType.values().length];
        int i = 0;
        for(RobotType type : RobotType.values()){
            number[i] = BOARD.numberTasks(type);
            i++;
        }
        return number;
    }
}