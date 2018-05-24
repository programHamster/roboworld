package by.roboworld.model.bean.board;

import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.generalTask.DieTask;
import by.roboworld.model.bean.task.generalTask.GeneralTask;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TaskBoardTest<T extends Task> {
    private final Board<T> board;
    private final T task;

    public TaskBoardTest(Board<T> board, T task) {
        this.board = board;
        this.task = task;
    }

    @Parameterized.Parameters
    public static List<Object[]> boards() {
        return Arrays.asList(new Object[][]{
                { new TaskBoard<BackEndTask>(), new BackEndTask()},
                { new TaskBoard<FrontEndTask>(), new FrontEndTask()},
                { new TaskBoard<HRTask>(), new HRTask()},
                { new TaskBoard<GeneralTask>(), new DieTask()}
        });
    }

    @Test
    public void addAndGet() {
        // act
        boolean success = board.add(task);
        Task task = board.get();
        // assert
        assertEquals(this.task, task);
        assertTrue(success);
    }

    @Test(expected = NullPointerException.class)
    public void add_null() {
        // act
        board.add(null);
    }

    @Test
    public void poll() {
        // arrange
        int numberTasks = board.size();
        // act
        board.add(task);
        Task task = board.poll();
        // assert
        assertEquals(board.size(), numberTasks);
        assertEquals(this.task, task);
    }

    @Test
    public void size() {
        // arrange
        int number = 10;
        int oldSize = board.size();
        // act
        for(int i=0; i<number; i++){
            board.add(task);
        }
        int newSize = oldSize + number;
        // assert
        assertEquals(newSize, board.size());
    }
}