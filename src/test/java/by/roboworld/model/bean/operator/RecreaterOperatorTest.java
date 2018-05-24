package by.roboworld.model.bean.operator;

import by.roboworld.exception.RobotAlreadyExistException;
import by.roboworld.exception.TaskNotFeasibleException;
import by.roboworld.model.bean.board.SharedBoard;
import by.roboworld.model.bean.robot.GeneralRobot;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.generalTask.DieTask;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import by.roboworld.model.bean.tracker.TrackerInitiator;
import by.roboworld.model.facroty.RobotType;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Theories.class)
public class RecreaterOperatorTest {

    @Test
    public void createRobot() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // act
        Robot robot = operator.createRobot(RobotType.GENERAL);
        Robot robot2 = operator.createRobot(RobotType.BACK_END_DEVELOPER, "name");
        // assert
        assertNotNull(robot);
        assertTrue(robot instanceof GeneralRobot);
        assertNotNull(robot2);
        assertTrue(robot2 instanceof BackEndDeveloperRobot);
        assertNotEquals(robot, robot2);
    }

    @Test(expected = RobotAlreadyExistException.class)
    public void createRobot_sameRobotNamesDiffTypes() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        final String NAME = "name";
        // act
        operator.createRobot(RobotType.FRONT_END_DEVELOPER, NAME);
        operator.createRobot(RobotType.BACK_END_DEVELOPER, NAME);
    }

    @Test(expected = NullPointerException.class)
    public void createRobot_null() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // act
        operator.createRobot(null);
    }

    @Test
    public void broadcastTask() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        TrackerInitiator trackerInitiator = mock(TrackerInitiator.class);
        operator.setTrackerInitiator(trackerInitiator);
        Task task = new BackEndTask();
        // act
        boolean success = operator.broadcastTask(task);
        // assert
        assertTrue(success);
        assertEquals(task, SharedBoard.getInstance().get(RobotType.BACK_END_DEVELOPER));
        verify(trackerInitiator).control(any(BroadcastEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void broadcastTask_null() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // act
        operator.broadcastTask(null);
    }

    @DataPoints
    public static Object[][] tasks = new Object[][]{
            {RobotType.BACK_END_DEVELOPER, new BackEndTask()},
            {RobotType.FRONT_END_DEVELOPER, new FrontEndTask()},
            {RobotType.HR, new HRTask()},
            {RobotType.GENERAL, new DieTask()}
    };

    @Theory
    public void assignTask(Object... tasks) {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        Robot robot = operator.createRobot((RobotType)tasks[0]);
        // assert
        assertTrue(operator.assignTask((Task)tasks[1], robot.getName()));
    }

    @Test
    public void assignTask_generalTaskForAllRobot() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        Task generalTask = new DieTask();
        for(RobotType type : RobotType.values()){
            Robot robot = operator.createRobot(type);
            // assert
            assertTrue(operator.assignTask(generalTask, robot.getName()));
        }
    }

    @Test(expected = TaskNotFeasibleException.class)
    public void assignTask_specialTaskForGeneralRobot() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        Robot generalRobot = operator.createRobot(RobotType.GENERAL);
        Task backTask = new BackEndTask();
        // assert
        assertTrue(operator.assignTask(backTask, generalRobot.getName()));
    }

    @Test(expected = TaskNotFeasibleException.class)
    public void assignTask_wrongTaskType() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        Robot robot = operator.createRobot(RobotType.BACK_END_DEVELOPER);
        Task task = new FrontEndTask();
        // act
        boolean success = operator.assignTask(task, robot.getName());
        // assert
        assertTrue(success);
    }

    @Test(expected = NullPointerException.class)
    public void assignTask_null() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // act
        operator.broadcastTask(null);
    }

}