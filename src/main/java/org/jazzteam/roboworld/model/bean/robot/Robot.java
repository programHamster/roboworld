package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.task.Task;

public interface Robot extends Runnable {
    void addTask(Task task);
    void wakeUp();
    boolean isAlive();
}
