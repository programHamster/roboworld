package org.jazzteam.roboworld.command;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.output.OutputEnum;
import org.jazzteam.roboworld.output.OutputInformation;
import org.jazzteam.roboworld.output.RoboWorldEvent;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CommandTest {
    private StringWriter stringWriter;

    @Before
    public void initOutput(){
        stringWriter = new StringWriter();
        OutputInformation.installOutput(OutputEnum.WRITER);
        OutputInformation.setWriter(stringWriter);
    }

    @Test
    public void execute_createRobot() throws UnsupportedException {
        String robotName = null;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter(Constants.PARAM_NAME_ROBOT_TYPE))
                .thenReturn(Constants.BACK_END_ROBOT_VALUE);
        when(mockRequest.getParameter(Constants.PARAM_NAME_ROBOT_NAME))
                .thenReturn(robotName);
        Operator mockOperator = mock(Operator.class);
        Robot robot = spy(new BackEndDeveloperRobot());
        when(mockOperator.createRobot(any(RobotType.class), anyString())).thenReturn(robot);
        Command.CREATE_ROBOT.execute(mockRequest, mockOperator);
        verify(mockOperator).createRobot(RobotType.BACK_END_DEVELOPER , robotName);
        assertTrue(stringWriter.toString().startsWith("robot:"));
    }

    @Test
    public void execute_createTask() throws UnsupportedException {
        String taskName = null;
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter(Constants.PARAM_NAME_TASK_TYPE))
                .thenReturn(Constants.BACK_END_TASK_VALUE);
        when(mockRequest.getParameter(Constants.PARAM_NAME_TASK_NAME))
                .thenReturn(taskName);
        Command.CREATE_TASK.execute(mockRequest, null);
        assertTrue(stringWriter.toString().startsWith("task:"));
    }

    @Test
    public void execute_giveTask_assignTask() throws UnsupportedException {
        Robot robot = new BackEndDeveloperRobot();
        Task task = new BackEndTask();
        task.setName("someTask");
        execute_giveTask("true", robot, null, task);
        assertEquals("task:" + "assign task " + task.getName(), stringWriter.toString());
    }

    @Test
    public void execute_giveTask_broadcastTask() throws UnsupportedException {
        Task task = new BackEndTask();
        task.setName("someTask");
        execute_giveTask("false", null, Constants.BACK_END_ROBOT_VALUE, task);
        assertEquals("task:" + "broadcast task " + task.getName(), stringWriter.toString());
    }

    /**
     * @param checkbox "true" if is assign task to particular robot or "false" if is broadcast
     * @param robot robot for assign a task
     * @param robotType needed for broadcast
     * @param task task for assign
     */
    private void execute_giveTask(String checkbox, Robot robot, String robotType, Task task)
            throws UnsupportedException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter(Constants.PARAM_NAME_TASK_NAME))
                .thenReturn(task.getName());
        when(mockRequest.getParameter(Constants.PARAM_NAME_CHECKBOX))
                .thenReturn(checkbox);
        when(mockRequest.getParameter(Constants.PARAM_NAME_ROBOT_TYPE))
                .thenReturn(robotType);
        TaskHolder.getInstance().putTask(task);
        Operator mockOperator = mock(Operator.class);
        when(mockOperator.get(anyString()))
                .thenReturn(robot);
        when(mockOperator.assignTask(any(Task.class), anyString()))
                .then(mock -> {
                    OutputInformation.write("assign task " + ((Task)mock.getArguments()[0]).getName(),
                            RoboWorldEvent.TASK);
                    return null;
                });
        when(mockOperator.broadcastTask(any(Task.class)))
                .then(mock -> {
                    OutputInformation.write("broadcast task " + ((Task)mock.getArguments()[0]).getName(),
                            RoboWorldEvent.TASK);
                    return null;
                });
        Command.GIVE_TASK.execute(mockRequest, mockOperator);
    }
}