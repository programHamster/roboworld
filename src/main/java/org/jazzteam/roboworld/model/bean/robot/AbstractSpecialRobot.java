package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.TaskNotFeasibleException;

public abstract class AbstractSpecialRobot extends AbstractRobot implements SpecialRobot {

    public void addTask(Task task) {
        if(task == null){
            throw new TaskIsNullException();
        }
        if(checkTaskFeasibility(task)){
            super.addTask(task);
        } else {
            throw new TaskNotFeasibleException(getName(), task);
        }
    }

    protected boolean checkTaskFeasibility(Task task){
        boolean isFeasible = true;
        for(Class<?> taskClass : getRobotType().getFeasibleTasks()){
            if(!taskClass.isInstance(task)){
                isFeasible = false;
                break;
            }
        }
        return super.checkTaskFeasibility(task) || isFeasible;
    }

    protected Task getSharedSpecialTask(){
        return SharedBoard.getBoard(getRobotType()).poll();
    }

}
