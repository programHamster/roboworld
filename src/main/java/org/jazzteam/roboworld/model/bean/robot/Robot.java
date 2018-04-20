package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.facroty.RobotType;

public interface Robot {
    void start();
    void setName(String name);
    String getName();
    boolean addTask(Task task);
    void wakeUp();
    boolean isAlive();
    boolean isRunning();
    boolean isDie();
    RobotType getRobotType();
}
