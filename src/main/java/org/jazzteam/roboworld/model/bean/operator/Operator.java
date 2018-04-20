package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.Map;

public interface Operator {
    Map<String, Robot> getRobots();
    Robot createRobot(RobotType type);
    Robot createRobot(RobotType type, String name) throws RobotAlreadyExistException;
    int countRobots(RobotType type);
    int countRobots();
    Robot get(String robotName);
    boolean addTracker(Tracker tracker);
    boolean assignTask(Task task, String robotName);
    boolean broadcastTask(Task task);

}
