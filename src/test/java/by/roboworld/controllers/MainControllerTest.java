package by.roboworld.controllers;

import by.roboworld.Constants;
import by.roboworld.exception.TaskNotFeasibleException;
import by.roboworld.exception.TaskNotFoundException;
import by.roboworld.exception.unsupported.UnsupportedRobotTypeException;
import by.roboworld.exception.unsupported.UnsupportedTaskException;
import by.roboworld.model.bean.operator.Operator;
import by.roboworld.model.bean.operator.RecreaterOperator;
import by.roboworld.model.bean.robot.GeneralRobot;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.TaskHolder;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import by.roboworld.model.facroty.RobotType;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Theories.class)
public class MainControllerTest {

    @Test
    public void getRobots() {
        // arrange
        MainController controller = new MainController();
        Operator operator = mock(Operator.class);
        Map<String, Robot> robots = new HashMap<>();
        Robot robot = new BackEndDeveloperRobot();
        robots.put(robot.getName(), robot);
        when(operator.getRobots()).thenReturn(robots);
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        Map<String, Robot> result = controller.getRobots();
        // assert
        assertEquals(robots, result);
    }

    @Test
    public void getTasks() {
        // arrange
        Task backEndTask = new BackEndTask();
        Task frontEndTask = new FrontEndTask();
        TaskHolder.getInstance().putTask(backEndTask);
        TaskHolder.getInstance().putTask(frontEndTask);
        MainController controller = new MainController();
        // act
        Map<RobotType, Map<String, Task>> result = controller.getTasks();
        // assert
        assertTrue(result.get(RobotType.BACK_END_DEVELOPER).containsKey(backEndTask.getName()));
        assertTrue(result.get(RobotType.FRONT_END_DEVELOPER).containsKey(frontEndTask.getName()));
    }

    @Test
    public void createRobot() throws UnsupportedRobotTypeException {
        // arrange
        MainController controller = new MainController();
        Operator operator = mock(Operator.class);
        when(operator.createRobot(any(RobotType.class), anyString())).thenReturn(new GeneralRobot());
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        controller.createRobot(Constants.BACK_END_ROBOT_VALUE, "");
        // assert
        verify(operator).createRobot(any(RobotType.class), anyString());
    }

    @DataPoints
    public static Object[][] data = new Object[][]{
            // task name, task implementation, robot type
            {"backEndTask", Constants.BACK_END_TASK_VALUE, RobotType.BACK_END_DEVELOPER, },
            {"frontEndTask", Constants.FRONT_END_TASK_VALUE, RobotType.FRONT_END_DEVELOPER},
            {"HRTask", Constants.HR_TASK_VALUE, RobotType.HR},
            {"dieTask", Constants.DIE_TASK_VALUE, RobotType.GENERAL}
    };

    @Theory
    public void createTask(Object... data) throws UnsupportedTaskException {
        // arrange
        final String TASK_NAME = (String)data[0];
        MainController controller = new MainController();
        controller.createTask((String)data[1], TASK_NAME);
        Task task = TaskHolder.getInstance().getTask(TASK_NAME, (RobotType)data[2]);
        assertNotNull(task);
        assertEquals(task.getName(), TASK_NAME);
    }

    @Test
    public void assignTask() {
        // arrange
        final String TASK_NAME = "taskName";
        final String ROBOT_NAME = "robotName";
        MainController controller = new MainController();
        Task task = new BackEndTask(TASK_NAME);
        TaskHolder.getInstance().putTask(task);
        Operator operator = mock(Operator.class);
        when(operator.get(ROBOT_NAME)).thenReturn(new BackEndDeveloperRobot());
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        controller.assignTask(TASK_NAME, ROBOT_NAME);
        // assert
        verify(operator).assignTask(task, ROBOT_NAME);
    }

    @Test(expected = TaskNotFeasibleException.class)
    public void assignTask_taskNotFeasible(){
        // arrange
        final String TASK_NAME = "hrTask";
        final String ROBOT_NAME = "robotName";
        MainController controller = new MainController();
        Task task = new HRTask(TASK_NAME);
        TaskHolder.getInstance().putTask(task);
        Operator operator = mock(Operator.class);
        when(operator.get(ROBOT_NAME)).thenReturn(new BackEndDeveloperRobot());
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        controller.assignTask(TASK_NAME, ROBOT_NAME);
    }

    @Test(expected = TaskNotFoundException.class)
    public void assignTask_taskNotExists(){
        // arrange
        final String TASK_NAME = "noExistingTask";
        final String ROBOT_NAME = "robotName";
        MainController controller = new MainController();
        Operator operator = mock(Operator.class);
        when(operator.get(ROBOT_NAME)).thenReturn(new BackEndDeveloperRobot());
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        controller.assignTask(TASK_NAME, ROBOT_NAME);
    }

    @Test
    public void broadcastTask() throws UnsupportedRobotTypeException {
        // arrange
        final String TASK_NAME = "taskName";
        final String ROBOT_TYPE_NAME = Constants.BACK_END_ROBOT_VALUE;
        MainController controller = new MainController();
        Task task = new BackEndTask(TASK_NAME);
        TaskHolder.getInstance().putTask(task);
        Operator operator = mock(Operator.class);
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        controller.broadcastTask(TASK_NAME, ROBOT_TYPE_NAME);
        // assert
        verify(operator).broadcastTask(task);
    }

    @Test(expected = TaskNotFoundException.class)
    public void broadcastTask_taskNotExists() throws UnsupportedRobotTypeException {
        // arrange
        final String TASK_NAME = "noExistingTask";
        final String ROBOT_TYPE_NAME = Constants.BACK_END_ROBOT_VALUE;
        MainController controller = new MainController();
        Operator operator = new RecreaterOperator();
        ReflectionTestUtils.setField(controller, "operator", operator);
        // act
        controller.broadcastTask(TASK_NAME, ROBOT_TYPE_NAME);
    }

}