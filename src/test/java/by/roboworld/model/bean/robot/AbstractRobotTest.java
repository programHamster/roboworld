package by.roboworld.model.bean.robot;

import by.roboworld.exception.RobotActuationException;
import by.roboworld.exception.RobotDeadException;
import by.roboworld.exception.TaskNotFeasibleException;
import by.roboworld.model.bean.board.SharedBoard;
import by.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import by.roboworld.model.bean.robot.specialRobot.FrontEndDeveloperRobot;
import by.roboworld.model.bean.robot.specialRobot.HRRobot;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.generalTask.DieTask;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import by.roboworld.model.facroty.RobotType;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class AbstractRobotTest {
    private static final SharedBoard board = SharedBoard.getInstance();
    private static final BackEndTask backEndTask = new BackEndTask();

    @Test(expected = RobotActuationException.class)
    public void activation_twice() {
        AbstractRobot robot = new GeneralRobot();
        robot.activation();
        robot.activation();
    }

    @Test
    public void work_whenRobotHaveTask() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        robot.addTask(task);
        // act
        robot.work();
        // assert
        assertNull(robot.getTask());
    }

    // because waiting for a task from another thread is enabled
    @Test(expected = IllegalMonitorStateException.class)
    public void work_whenRobotHaveNotTask() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.work();
        // assert
        assertNull(robot.getTask());
    }

    @DataPoints
    public static Object[][] forTest_takeSharedTask_robotTaskTypeResult = new Object[][]{
            // the general robot takes a general task
            {new GeneralRobot(), new DieTask(), RobotType.GENERAL, null},
            // the general robot don't take a special task
            {new GeneralRobot(), backEndTask, RobotType.BACK_END_DEVELOPER, backEndTask},
            // the special robot takes a general task
            {new BackEndDeveloperRobot(), new DieTask(), RobotType.GENERAL, null},
            // the special robot takes a special task
            {new BackEndDeveloperRobot(), new BackEndTask(), RobotType.BACK_END_DEVELOPER, null}
    };

    @Theory
    public void takeSharedTask(Object... data) {
        // arrange
        AbstractRobot robot = (AbstractRobot) data[0];
        Task task = (Task)data[1];
        RobotType taskType = (RobotType)data[2];
        Task result;
        // act
        synchronized (board){
            board.put(task);
            robot.takeSharedTask();
            result = board.poll(taskType);
        }
        // assert
        assertSame(result, data[3]);
    }

    @Test(expected = RobotDeadException.class)
    public void shutdown() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        // act
        robot.shutdown();
        // assert
        assertTrue(robot.addTask(task));
    }

    @Test//(expected = RobotDeadException.class)
    public void shutdown_robotStarted() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        robot.addTask(task);
        // act
        robot.start();
        robot.shutdown();
    }

    @Test(expected = RobotActuationException.class)
    public void start_twice() {
        // arrange
        Robot robot = new GeneralRobot();
        // act
        robot.start();
        // assert
        robot.start();
    }

    @Test(expected = RobotDeadException.class)
    public void start_afterShutdown() {
        // arrange
        AbstractRobot robot = new GeneralRobot();
        // act
        robot.shutdown();
        // assert
        robot.start();
    }

    @Test(expected = NullPointerException.class)
    public void setName_null() {
        // arrange
        Robot robot = new GeneralRobot();
        // assert
        robot.setName(null);
    }

    @Test
    public void getName() {
        // arrange
        Robot robot = new GeneralRobot();
        String name = "someName";
        // act
        robot.setName(name);
        // assert
        assertEquals(robot.getName(), name);
    }

    @Test
    public void addTask() {
        // arrange
        Robot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        // assert
        assertTrue(robot.addTask(task));
    }

    @Test(expected = TaskNotFeasibleException.class)
    public void addTask_specialTaskToGeneralRobot() {
        // arrange
        Robot robot = new GeneralRobot();
        Task task = new BackEndTask();
        // assert
        robot.addTask(task);
    }

    @Test(expected = TaskNotFeasibleException.class)
    public void addTask_wrongSpecialTask() {
        // arrange
        Robot robot = new BackEndDeveloperRobot();
        Task task = new FrontEndTask();
        // assert
        robot.addTask(task);
    }

    @Test
    public void addTask_GeneralTaskToBackEndRobot() {
        // arrange
        Robot robot = new BackEndDeveloperRobot();
        Task task = new DieTask();
        // assert
        assertTrue(robot.addTask(task));
    }

    @Test(expected = NullPointerException.class)
    public void addTask_null() {
        // arrange
        Robot robot = new GeneralRobot();
        // assert
        robot.addTask(null);
    }

    @Test
    public void getTask() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        robot.addTask(task);
        // assert
        assertEquals(robot.getTask(), task);
    }

    @Test
    public void getTask_whenRobotHaveNotTasks() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // assert
        assertNull(robot.getTask());
    }

    @Test(expected = RobotDeadException.class)
    public void getTask_whenRobotIsDead() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.shutdown();
        // assert
        robot.getTask();
    }

    @Test
    public void removeTask() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        robot.addTask(task);
        // assert
        assertEquals(robot.pollTask(), task);
    }

    @Test
    public void removeTask_whenRobotHaveNotTasks() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // assert
        assertNull(robot.pollTask());
    }

    @Test(expected = RobotDeadException.class)
    public void removeTask_whenRobotIsDead() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.shutdown();
        // assert
        robot.pollTask();
    }

    @Test(expected = RobotDeadException.class)
    public void wakeUp_whenRobotIsDead() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.shutdown();
        // assert
        robot.wakeUp();
    }

    @Test
    public void wakeUp() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // assert
        robot.wakeUp();
    }

    @Test
    public void wakeUp_whenRobotHaveLock() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        robot.addTask(task);
        // act
        robot.start();
        // assert
        robot.wakeUp();
    }

    // because there is no monitor on a robot lock
    // only a robot can stop itself
    @Test(expected = IllegalMonitorStateException.class)
    public void await() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // assert
        robot.await();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void await_whenRobotWork() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        robot.addTask(task);
        // act
        robot.start();
        // assert
        robot.await();
    }

    private static class TaskFeasibility {
        private AbstractRobot robot;
        private Task task;
        private boolean feasibility;

        private TaskFeasibility(AbstractRobot robot, Task task, boolean feasibility) {
            this.robot = robot;
            this.task = task;
            this.feasibility = feasibility;
        }
    }
    @DataPoints
    public static TaskFeasibility[] taskFeasibility = new TaskFeasibility[]{
        new TaskFeasibility(new BackEndDeveloperRobot(), new BackEndTask(), true),
        new TaskFeasibility(new FrontEndDeveloperRobot(), new FrontEndTask(), true),
        new TaskFeasibility(new HRRobot(), new HRTask(), true),
        new TaskFeasibility(new GeneralRobot(), new DieTask(), true),
        new TaskFeasibility(new BackEndDeveloperRobot(), new DieTask(), true),
        new TaskFeasibility(new FrontEndDeveloperRobot(), new DieTask(), true),
        new TaskFeasibility(new HRRobot(), new DieTask(), true),
        new TaskFeasibility(new BackEndDeveloperRobot(), new FrontEndTask(), false),
        new TaskFeasibility(new BackEndDeveloperRobot(), new HRTask(), false),
        new TaskFeasibility(new FrontEndDeveloperRobot(), new BackEndTask(), false),
        new TaskFeasibility(new FrontEndDeveloperRobot(), new HRTask(), false),
        new TaskFeasibility(new HRRobot(), new BackEndTask(), false),
        new TaskFeasibility(new HRRobot(), new FrontEndTask(), false),
        new TaskFeasibility(new GeneralRobot(), new BackEndTask(), false),
        new TaskFeasibility(new GeneralRobot(), new FrontEndTask(), false),
        new TaskFeasibility(new GeneralRobot(), new HRTask(), false)
    };

    @Theory
    public void checkTaskFeasibility(TaskFeasibility taskFeasibility) {
        // arrange
        AbstractRobot robot = taskFeasibility.robot;
        Task task = taskFeasibility.task;
        boolean feasibility = taskFeasibility.feasibility;
        // act
        boolean result = robot.checkTaskFeasibility(task);
        // assert
        assertEquals(result, feasibility);
    }

    @Test
    public void isDie_robotDidNotRun() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // assert
        assertTrue(!robot.isDie());
    }

    @Test
    public void isDie_FalseReturned() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.start();
        // assert
        assertTrue(!robot.isDie());
    }

    @Test
    public void isDie_TrueReturned() throws InterruptedException{
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new DieTask();
        // act
        robot.start();
        robot.addTask(task);
        TimeUnit.MILLISECONDS.sleep(100);
        // assert
        assertTrue(robot.isDie());
    }

    @Test
    public void isDie_afterShutdown() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.start();
        robot.shutdown();
        // assert
        assertTrue(robot.isDie());
    }

    @Test
    public void isAlive() throws InterruptedException{
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new DieTask();
        // act
        assertTrue(robot.isAlive());
        robot.start();
        assertTrue(robot.isAlive());
        robot.addTask(task);
        TimeUnit.MILLISECONDS.sleep(100);
        // assert
        assertTrue(!robot.isAlive());
    }

    @Test
    public void isAlive_afterShutdown() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        robot.shutdown();
        // assert
        assertTrue(!robot.isAlive());
    }

    @Test
    public void isRunning_beforeTask() throws InterruptedException{
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        // act
        assertTrue(!robot.isRunning());
        robot.start();
        TimeUnit.MILLISECONDS.sleep(100);
        // assert
        assertTrue(robot.isRunning());
    }

    @Test
    public void isRunning_afterDieTask() throws InterruptedException{
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task task = new DieTask();
        robot.start();
        // act
        robot.addTask(task);
        TimeUnit.MILLISECONDS.sleep(100);
        // asset
        assertTrue(!robot.isRunning());
    }
}