package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.exception.RobotNotFoundException;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.Map;

public interface Operator {
    Map<String, Robot> getRobots();
    Robot createRobot(RobotType type);
    Robot createRobot(RobotType type, String name) throws RobotAlreadyExistException;
    int countRobots(RobotType type);
    int countRobots();
    void assignTask(Task task, Robot robot);
    void assignTask(Task task, String name);
    void broadcastTask(Task task, RobotType type);

    /**
     * Only for general tasks
     *
     * @param task general task
     */
    void broadcastTask(Task task);

}
