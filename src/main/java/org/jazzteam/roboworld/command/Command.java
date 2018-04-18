package org.jazzteam.roboworld.command;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.TaskNotFoundException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.output.OutputWriter;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.model.facroty.RobotTypeFactory;
import org.jazzteam.roboworld.model.facroty.TaskFactory;
import org.jazzteam.roboworld.output.RoboWorldEvent;

import javax.servlet.http.HttpServletRequest;

public enum Command {
    CREATE_ROBOT{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            RobotType robotType = getRobotType(request);
            String robotName = request.getParameter(Constants.PARAM_NAME_ROBOT_NAME);
            Robot robot;
            if(robotName == null || (robotName = robotName.trim()).isEmpty()){
                robot = operator.createRobot(robotType);
            } else {
                robot = operator.createRobot(robotType, robotName);
            }
            OutputWriter.write("Robot \"" + robot.getName() + "\" created", RoboWorldEvent.ROBOT);
        }
    }, CREATE_TASK{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            String taskTypeName = request.getParameter(Constants.PARAM_NAME_TASK_TYPE);
            String taskName = request.getParameter(Constants.PARAM_NAME_TASK_NAME);
            Task task = TaskFactory.getTaskFromFactory(taskTypeName, taskName);
            TaskHolder.getInstance().putTask(task);
            OutputWriter.write("Task \"" + task.getName() + "\" created", RoboWorldEvent.TASK);
        }
    }, GIVE_TASK{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            String taskName = request.getParameter(Constants.PARAM_NAME_TASK_NAME);
            Task task;
            try {
                if(getCheckbox(request, Constants.PARAM_NAME_CHECKBOX)){
                    String robotName = request.getParameter(Constants.PARAM_NAME_ROBOT_NAME);
                    Robot robot = operator.get(robotName);
                    task = TaskHolder.getInstance().getTask(taskName, robot.getRobotType());
                    operator.assignTask(task, robotName);
                } else {
                    RobotType robotType = getRobotType(request);
                    task = TaskHolder.getInstance().getTask(taskName, robotType);
                    operator.broadcastTask(task, robotType);
                }
            } catch (TaskIsNullException e) {
                throw new TaskNotFoundException(taskName, " or robot type is wrong");
            }
        }
    };

    public abstract void execute(HttpServletRequest request, Operator operator) throws UnsupportedException;

    protected static RobotType getRobotType(HttpServletRequest request) throws UnsupportedException{
        String robotTypeName = request.getParameter(Constants.PARAM_NAME_ROBOT_TYPE);
        return RobotTypeFactory.getRobotTypeFromFactory(robotTypeName);
    }

    protected static boolean getCheckbox(HttpServletRequest request, String paramName){
        String checkboxValue = request.getParameter(paramName);
        boolean checkbox = false;
        if(checkboxValue != null){
            checkbox = Boolean.valueOf(checkboxValue);
        }
        return checkbox;
    }
}
