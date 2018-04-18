package org.jazzteam.roboworld.command;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.model.exception.TaskNotFoundException;
import org.jazzteam.roboworld.model.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.model.facroty.OutputFactory;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.model.facroty.RobotTypeFactory;
import org.jazzteam.roboworld.model.facroty.TaskFactory;

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
            OutputFactory.println("Robot \"" + robot.getName() + "\" created");
        }
    }, CREATE_TASK{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            String taskTypeName = request.getParameter(Constants.PARAM_NAME_TASK_TYPE);
            String taskName = request.getParameter(Constants.PARAM_NAME_TASK_NAME);
            Task task = TaskFactory.getTaskFromFactory(taskTypeName, taskName);
            TaskHolder.getInstance().putTask(task);
            OutputFactory.println("Task \"" + task.getName() + "\" created");
        }
    }, GIVE_TASK{
        public void execute(HttpServletRequest request, Operator operator) throws UnsupportedException{
            RobotType robotType = getRobotType(request);
            String taskName = request.getParameter(Constants.PARAM_NAME_TASK_NAME);
            Task task = TaskHolder.getInstance().getTask(taskName, robotType);
            if(task == null){
                throw new TaskNotFoundException(taskName, " or type is wrong");
            }
            String robotName = null;
            if(getCheckbox(request, Constants.PARAM_NAME_CHECKBOX)){
                robotName = request.getParameter(Constants.PARAM_NAME_ROBOT_NAME);
            }
            if(robotName != null && !(robotName = robotName.trim()).isEmpty()){
                operator.assignTask(task, robotName);
            } else {
                operator.broadcastTask(task, robotType);
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
