package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.TaskBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.RobotType;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;

import java.util.HashMap;
import java.util.Map;

public class Operator {
    private Map<String, Robot> robots = new HashMap<>();

    public void createRobot(RobotType type, String name) throws RobotAlreadyExistException{
        Robot robot = type.getRobot();
        if(robots.putIfAbsent(name, robot) != null){
            throw new RobotAlreadyExistException(name);
        }
        new Thread(robot, name).start();
    }

    public void broadcastTask(Task task){
        TaskBoard.addCommonTask(task);
        robots.forEach((id, robot) -> robot.wakeUp());
    }

    public void assignTask(Task task, String name){
        Robot robot = robots.get(name);
        robot.addTask(task);
        robot.wakeUp();
    }
}
