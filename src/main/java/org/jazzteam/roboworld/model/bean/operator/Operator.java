package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.TaskBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.RobotType;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.exception.RobotNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Operator {
    private Map<String, Robot> robots = new ConcurrentHashMap<>();

    public void createRobot(RobotType type, String name) throws RobotAlreadyExistException{
        Robot robot = type.getRobot();
        if(robots.putIfAbsent(name, robot) != null){
            throw new RobotAlreadyExistException(name);
        }
        new Thread(robot, name).start();
    }

    public void broadcastTask(Task task){
        TaskBoard.addCommonTask(task);
        robots.forEach((name, robot) -> {
            if(robot.isAlive()){
                robot.wakeUp();
            } else {
                robots.remove(name);
            }
        });
    }

    public void assignTask(Task task, String name) throws RobotNotFoundException{
        Robot robot = getRobot(name);
        robot.addTask(task);
        robot.wakeUp();
    }

    /**
     * Returns a robot from the map
     *
     * @param name name of robot
     * @return robot with specified name
     * @throws RobotNotFoundException throws if the robot was not found
     * @throws RobotDeadException throws if the robot was found but he was dead
     */
    private Robot getRobot(String name) throws RobotNotFoundException{
        Robot robot = robots.get(name);
        if(robot == null){
            throw new RobotNotFoundException();
        } else if(!robot.isAlive()){
            robots.remove(name);
            throw new RobotDeadException("the robot named \"" + name + "\" is dead");
        }
        return robot;
    }
}
