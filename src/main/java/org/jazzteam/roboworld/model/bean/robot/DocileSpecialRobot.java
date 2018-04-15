package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotDeadException;

public abstract class DocileSpecialRobot extends AbstractSpecialRobot {

    protected Task work() throws RobotDeadException{
        Task task = super.work();
        if(task != null){
            System.out.println("The robot \"" + getName() + "\" completed the task \"" + task.getName() + "\"");
        }
        return task;
    }

    protected boolean takeSharedTask() throws RobotDeadException{
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

    @Override
    protected void shutdown(){
        super.shutdown();
        System.out.println("The robot \"" + Thread.currentThread().getName() + "\" is dead.");
    }

}
