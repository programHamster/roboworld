package by.roboworld.model.bean.board;

import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.generalTask.DieTask;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import by.roboworld.model.facroty.RobotType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class SharedBoardTest {
    private static final SharedBoard board = SharedBoard.getInstance();

    @Test
    public void getInstance() throws InterruptedException, ExecutionException {
        // arrange
        final int NUMBER_THREADS = 5;
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
        List<Callable<SharedBoard>> callables = new ArrayList<>(NUMBER_THREADS);
        final boolean[] result = new boolean[1];
        result[0] = true;
        for(int i=0; i<NUMBER_THREADS; i++){
            callables.add(SharedBoard::getInstance);
        }
        // act
        List<Future<SharedBoard>> futures = executor.invokeAll(callables);
        futures.stream().map(future -> {
            SharedBoard sharedBoard = null;
            try {
                sharedBoard = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return sharedBoard;
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
    public void put() {
        // arrange
        Task generalTask = new DieTask();
        Task backEndTask = new BackEndTask();
        Task frontEndTask = new FrontEndTask();
        Task hrTask = new HRTask();
        // act
        synchronized (board){
            board.put(generalTask);
            board.put(backEndTask);
            board.put(frontEndTask);
            board.put(hrTask);
            // assert
            assertEquals(generalTask, board.get(RobotType.GENERAL));
            assertEquals(backEndTask, board.poll(RobotType.BACK_END_DEVELOPER));
            assertEquals(frontEndTask, board.get(RobotType.FRONT_END_DEVELOPER));
            assertEquals(hrTask, board.get(RobotType.HR));
            // because the back end developer haven't tasks
            assertEquals(generalTask, board.get(RobotType.BACK_END_DEVELOPER));
            assertNotEquals(hrTask, board.get(RobotType.FRONT_END_DEVELOPER));
        }
    }

    @Test(expected = NullPointerException.class)
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
        synchronized (board){
            numberBefore = board.numberTasks(RobotType.HR);
            board.put(hrTask);
            board.poll(RobotType.HR);
            numberAfter = board.numberTasks(RobotType.HR);
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
        synchronized (board){
            numberBefore = countNumberTasks();
            board.put(new DieTask());
            board.put(new BackEndTask());
            board.put(new FrontEndTask());
            board.put(new HRTask());
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
            number[i] = board.numberTasks(type);
            i++;
        }
        return number;
    }
}