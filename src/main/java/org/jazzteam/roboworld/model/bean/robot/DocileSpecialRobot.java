package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.task.Task;

public abstract class DocileSpecialRobot extends AbstractSpecialRobot {

    protected Task work() {
        Task task = super.work();

        return task;
    }

    protected boolean takeSharedTask() {
        boolean result;
        Task task = getSharedSpecialTask();
        if(task != null){
            addTask(task);
            result = true;
        } else {
            result = super.takeSharedTask();
        }
        return result;
    }

}
