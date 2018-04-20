package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.task.Task;

public abstract class AbstractSpecialRobot extends AbstractRobot implements SpecialRobot {

    protected boolean takeSharedTask() {
        boolean result;
        Task task = SharedBoard.getInstance().poll(getRobotType());
        if(task != null){
            result = addTask(task);
        } else {
            result = super.takeSharedTask();
        }
        return result;
    }

}
