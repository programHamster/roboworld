package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.GeneralTask;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.exception.TaskIsNullException;
import org.jazzteam.roboworld.model.exception.TaskNotFeasibleException;

public abstract class AbstractSpecialRobot extends GeneralRobot implements SpecialRobot {

    public void addTask(Task task) throws RobotDeadException{
        if(task == null){
            throw new TaskIsNullException();
        }
        if(isGeneralTask(task) || checkTaskFeasibility(task)){
            super.addTask(task);
        } else {
            throw new TaskNotFeasibleException(getName(), task);
        }
    }

    /**
     * The GeneralTask is a task that implements the interface <code>GeneralTask</code> in the first place.
     * Had to resort to reflection because interface the SpecialTask inherited from interface the GeneralTask and
     * their separation would be illogical.
     *
     * @param task checked task
     * @return <code>true</code> if the task is general, <code>false</code> otherwise
     */
    private static boolean isGeneralTask(Task task){
        Class<?>[] interfaces = getInterfaces(task.getClass());
        boolean result = false;
        for(Class<?> someInterface : interfaces){
            if(someInterface == GeneralTask.class){
                result = true;
                break;
            }
        }
        return result;
    }

    private static Class<?>[] getInterfaces(Class<?> someClass){
        Class<?>[] interfaces = someClass.getInterfaces();
        if(interfaces.length == 0){
            interfaces = getInterfaces(someClass.getSuperclass());
        }
        return interfaces;
    }

    private boolean checkTaskFeasibility(Task task){
        boolean isFeasible = true;
        for(Class<?> taskClass : getRobotType().getFeasibleTasks()){
            if(!taskClass.isInstance(task)){
                isFeasible = false;
                break;
            }
        }
        return isFeasible;
    }

    protected Task getSharedSpecialTask(){
        return SharedBoard.getBoard(getRobotType()).poll();
    }

}
