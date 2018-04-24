package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.exception.RobotActuationException;
import org.jazzteam.roboworld.exception.RobotDeadException;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.TaskNotFeasibleException;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.facroty.RobotType;

/**
 * This interface describes the behavior of any robot.
 */
public interface Robot {

    /**
     * After calling this method, the robot begins its life cycle.
     *
     * @throws RobotDeadException if the robot is already dead
     * @throws RobotActuationException if the robot was already started
     */
    void start();

    /**
     * Changes the name of this robot.
     *
     * @param name name of the robot
     */
    void setName(String name);

    /**
     * Returns this robot's name.
     *
     * @return this robot's name.
     */
    String getName();

    /**
     * Adds a task to the task queue of the robot. If the robot started to turn off
     * or already off, then throw the {@code RobotDeadException}. If the robot determines
     * that it will not be able to perform this task, it will throw the {@code TaskNotFeasibleException}.
     *
     * @param task the task to the execute
     * @return <code>true</code> if the task was added to the queue successfully
     *         and <code>false</code> otherwise
     * @throws TaskIsNullException if the specified task is null
     * @throws RobotDeadException if the robot started to turn off or already off
     * @throws TaskNotFeasibleException if the robot determines that it will not be able
     *                                  to perform this task
     */
    boolean addTask(Task task);

    /**
     * Returns the type to which this robot belongs.
     *
     * @return the type to which this robot belongs
     */
    RobotType getRobotType();

    /**
     * Awakes a robot from the standby mode.
     *
     * @throws RobotDeadException if the robot started to turn off or already off
     */
    void wakeUp();

    /**
     * Return <code>true</code> if the robot has created and not dead.
     * This corresponds to the activation and running stages.
     *
     * @return <code>true</code> if the robot has created and not dead;
     *          <code>false</code> otherwise.
     */
    boolean isAlive();

    /**
     * Return <code>true</code> if the robot has activated and not dead.
     * This corresponds to the running stage.
     *
     * @return <code>true</code> if the robot has activated and not dead;
     *          <code>false</code> otherwise.
     */
    boolean isRunning();

    /**
     * Returns <code>true</code> if the robot was ordered to die, but he did not have time to do it.
     * This corresponds to the shutdown and dead stages.
     *
     * @return <code>true</code> if the robot was to kill himself;
     *          <code>false</code> otherwise.
     */
    boolean isDie();
}
