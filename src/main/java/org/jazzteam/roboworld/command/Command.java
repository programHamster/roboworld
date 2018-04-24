package org.jazzteam.roboworld.command;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.RobotDeadException;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.TaskNotFeasibleException;
import org.jazzteam.roboworld.exception.TaskNotFoundException;
import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.model.facroty.RobotTypeFactory;
import org.jazzteam.roboworld.model.facroty.taskFactory.TaskFactory;
import org.jazzteam.roboworld.output.OutputWriter;
import org.jazzteam.roboworld.output.RoboWorldEvent;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * This enumeration describes the commands passed by the user.
 */
public enum Command {
    /**
     * Creates a robot with the passed type and name for the specified operator.
     */
    CREATE_ROBOT{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            RobotType robotType = getRobotType(request);
            String robotName = decodeURIComponent(request.getParameter(Constants.PARAM_NAME_ROBOT_NAME));
            Robot robot;
            robot = operator.createRobot(robotType, robotName);
            OutputWriter.write("Robot \"" + robot.getName() + "\" created", RoboWorldEvent.ROBOT);
        }
    },
    /**
     * Creates a task for robots of the passed type and with the passed name. A new task is saved in the
     * {@code TaskHolder} from which it can be assigned to perform.
     */
    CREATE_TASK{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            String taskImplementation = request.getParameter(Constants.PARAM_NAME_TASK_TYPE);
            String taskName = decodeURIComponent(request.getParameter(Constants.PARAM_NAME_TASK_NAME));
            Task task = TaskFactory.getTaskFromFactory(taskImplementation, taskName);
            TaskHolder.getInstance().putTask(task);
            OutputWriter.write("Task \"" + task.getName() + "\" created", RoboWorldEvent.TASK);
        }
    },
    /**
     * Assigns a task to a particular robot or broadcasts that it needs to be performed by a particular
     * type of robots.
     */
    GIVE_TASK{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            String taskName = decodeURIComponent(request.getParameter(Constants.PARAM_NAME_TASK_NAME));
            Task task;
            try {
                if(getCheckbox(request, Constants.PARAM_NAME_CHECKBOX)){
                    String robotName = decodeURIComponent(request.getParameter(Constants.PARAM_NAME_ROBOT_NAME));
                    Robot robot = operator.get(robotName);
                    task = TaskHolder.getInstance().getTask(taskName, robot.getRobotType());
                    if(task == null){
                        task = TaskHolder.getInstance().totalSearch(taskName);
                        if(task != null){
                            throw new TaskNotFeasibleException(robotName, task);
                        }
                    }
                    try {
                        operator.assignTask(task, robotName);
                    } catch (RobotDeadException e) {
                        OutputWriter.write(e.getMessage(), RoboWorldEvent.ROBOT);
                    }
                } else {
                    RobotType robotType = getRobotType(request);
                    task = TaskHolder.getInstance().getTask(taskName, robotType);
                    operator.broadcastTask(task);
                }
            } catch (TaskIsNullException e) {
                throw new TaskNotFoundException(taskName);
            }
        }
    };

    /**
     * Executes a specified command.
     *
     * @param request the request given by the user
     * @param operator the operator who will carry out the management of robots
     * @throws UnsupportedException if the command passed in the request is not supported
     */
    public abstract void execute(HttpServletRequest request, Operator operator) throws UnsupportedException;

    /**
     * Returns a type of robot passed in the request.
     *
     * @param request the request that the client sent
     * @return a type of robot passed in the request
     * @throws UnsupportedException if the specified robot name is wrong
     * @throws RobotTypeNotSpecifiedException if the specified name of the robot is <code>null</code>
     */
    protected static RobotType getRobotType(HttpServletRequest request) throws UnsupportedException{
        String robotTypeName = request.getParameter(Constants.PARAM_NAME_ROBOT_TYPE);
        return RobotTypeFactory.getRobotTypeFromFactory(robotTypeName);
    }

    /**
     * Returns a value of the checkbox with the specified name.
     *
     * @param request the request that the client sent
     * @param paramName name of the checkbox
     * @return a value of the checkbox with the specified name
     */
    protected static boolean getCheckbox(HttpServletRequest request, String paramName){
        String checkboxValue = request.getParameter(paramName);
        return Boolean.valueOf(checkboxValue);
    }

    /**
     * Decodes the passed UTF-8 String using an algorithm that's compatible with
     * JavaScript's <code>decodeURIComponent</code> function. Returns
     * <code>null</code> if the String is <code>null</code>.
     *
     * @param str The UTF-8 encoded String to be decoded
     * @return the decoded String
     */
    protected static String decodeURIComponent(String str){
        if (str == null) {
            return null;
        }
        String result;
        try {
            result = URLDecoder.decode(str, "UTF-8");
        }
        // This exception should never occur.
        catch (UnsupportedEncodingException e) {
            result = str;
        }
        return result;
    }
}
