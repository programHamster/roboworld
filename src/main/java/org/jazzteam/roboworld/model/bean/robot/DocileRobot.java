package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.TaskBoard;
import org.jazzteam.roboworld.model.bean.task.Task;

public class DocileRobot extends AbstractRobot {

    @Override
    protected void work(){
        Task task;
        while((task = getTask()) != null){
            System.out.println("The robot \"" + Thread.currentThread().getName() + "\" is doing the task " + task.getId());
            task.perform();
            if(isDie()){
                return;
            }
        }
        takeCommonTask();
    }

    private void takeCommonTask(){
        Task task = TaskBoard.getCommonTask();
        if(task != null){
            addTask(task);
        } else{
            await();
        }
    }

    @Override
    protected void shutdown(){
        super.shutdown();
        System.out.println("The robot \"" + Thread.currentThread().getName() + "\" is dead.");
    }

}
