package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.TaskBoard;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotDeadException;

public class DocileRobot extends AbstractRobot {

    @Override
    protected void work(){
        String robotName = Thread.currentThread().getName();
        try {
            Task task;
            while((task = getTask()) != null){
                System.out.println("The robot \"" + robotName + "\" is doing the task " + task.getId());
                task.perform();
            }
            takeCommonTask();
        } catch (RobotDeadException e) {
            System.out.println("The robot \"" + robotName + "\" is dead.");
        }
    }

    private void takeCommonTask(){
        Task task = TaskBoard.getCommonTask();
        if(task != null){
            addTask(task);
        } else{
            await();
        }
    }

}
