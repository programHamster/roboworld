package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.facroty.RobotType;

public interface Robot {
    void start();
    void setName(String name);
    String getName();
    void addTask(Task task) throws RobotDeadException;
    void wakeUp() throws RobotDeadException;
    boolean isAlive();
    boolean isRunning();
    boolean isDie();
    RobotType getRobotType();
}
