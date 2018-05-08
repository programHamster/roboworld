package org.jazzteam.roboworld.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.Setting;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainControllerTest {
    private final MainController mainServlet = new MainController();

    @Before
    public void initServlet(){
        initServlet(mainServlet);
    }

    @Test
    public void init() throws NoSuchFieldException, IllegalAccessException {
        // act
        Operator operator = getOperator(mainServlet);
        // assert
        assertTrue(operator.getTrackers().size() > 0);
        assertNotNull(operator);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void doGet_returnsTasks() throws IOException {
        // arrange
        List<Task> checkedTasks = Arrays.asList(new BackEndTask(), new FrontEndTask(), new HRTask(), new DieTask());
        checkedTasks.forEach(task -> TaskHolder.getInstance().putTask(task));
        // act
        Map<?, ?> data = getDataFromServlet(Constants.PARAM_VALUE_TASKS_INIT);
        for (Object mapByType : data.values()) {
            Set<String> taskNames = ((Map) mapByType).keySet();
            for (String taskName : taskNames) {
                // assert
                assertTrue(checkedTasks.stream().anyMatch(task -> task.getName().equals(taskName)));
            }
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void doGet_returnsRobots() throws IOException, NoSuchFieldException, IllegalAccessException {
        Operator operator = getOperator(mainServlet);
        List<Robot> robots = Arrays.asList(
                operator.createRobot(RobotType.BACK_END_DEVELOPER),
                operator.createRobot(RobotType.FRONT_END_DEVELOPER),
                operator.createRobot(RobotType.HR),
                operator.createRobot(RobotType.GENERAL));
        Map<?, ?> data = getDataFromServlet(Constants.PARAM_VALUE_ROBOTS_INIT);
        for(String robotName : (Set<String>)data.keySet()){
            assertTrue(robots.stream().anyMatch(robot -> robot.getName().equals(robotName)));
        }
    }

    private Map<?, ?> getDataFromServlet(String parameterValue) throws IOException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter(Constants.PARAM_NAME_INIT)).thenReturn(parameterValue);
        ObjectMapper mapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(printWriter);
        mainServlet.doGet(mockRequest, mockResponse);
        printWriter.flush();
        return mapper.readValue(stringWriter.toString(), Map.class);
    }

    @Test
    public void destroy() throws NoSuchFieldException, IllegalAccessException {
        mainServlet.destroy();
        assertNull(getOperator(mainServlet));
    }

    private static Operator getOperator(MainController mainController)
            throws NoSuchFieldException, IllegalAccessException {
        Field operatorField = mainController.getClass().getDeclaredField("operator");
        operatorField.setAccessible(true);
        return (Operator)operatorField.get(mainController);
    }

    private static void initServlet(MainController mainController){
        ServletConfig mockConfig = mock(ServletConfig.class);
        when(mockConfig.getInitParameter(Constants.INIT_PARAM_NAME_OPERATOR)).
                thenReturn(Setting.INIT_PARAM_VALUE_OPERATOR);
        when(mockConfig.getInitParameter(Constants.INIT_PARAM_NAME_OPERATOR_ADDITION_PARAM)).
                thenReturn(Setting.INIT_PARAM_VALUE_OPERATOR_ADDITION_PARAM);
        when(mockConfig.getInitParameter(Constants.INIT_PARAM_NAME_TRACKER)).
                thenReturn(Setting.INIT_PARAM_VALUE_TRACKER);
        when(mockConfig.getInitParameter(Constants.INIT_PARAM_NAME_TRACKER_ADDITION_PARAM)).
                thenReturn(Setting.INIT_PARAM_VALUE_TRACKER_PERIOD);
        mainController.init(mockConfig);
    }

}