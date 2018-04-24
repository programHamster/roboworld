package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.exception.RobotDeadException;
import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.task.Task;

/**
 * This class corrects the behavior of robots with a special purpose.
 */
public abstract class AbstractSpecialRobot extends AbstractRobot implements SpecialRobot {

    /**
     * Takes tasks primarily from the queue of special tasks and only then from general-purpose tasks.
     *
     * @return <code>true</code> if some task is found, <code>false</code> otherwise
     * @throws RobotDeadException if call the method after shutdown the robot and a task was found
     */
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
