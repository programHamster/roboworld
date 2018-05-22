package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.exception.RobotDeadException;
import org.jazzteam.roboworld.exception.RobotNotFoundException;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.notSpecified.RobotNameNotSpecifiedException;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.List;
import java.util.Map;

/**
 * The {@code Operator} is used to control robots. He creates them and assigns tasks.
 * He can include trackers to monitor for robots.
 */
public interface Operator {

    /**
     * Returns the {@code Map} contains all robots under the control of the operator.
     * If the operator hasn't robots, this method must returns an empty {@code Map}.
     * The returned {@code Map} is unmodifiable because only the operator can change it.
     *
     * @return the {@code Map} contains all robots under the control of the operator
     */
    Map<String, Robot> getRobots();

    /**
     * Creates and returns a new robot of the specified type. The created robot will
     * be started immediately. The name for a new robot will be generated automatically.
     *
     * @param type type of the robot
     * @return a new robot
     */
    Robot createRobot(RobotType type);

    /**
     * Creates and returns a new robot of the specified type and name. The created robot will
     * be started immediately. If the specified name is {@code null} or empty, a unique random
     * name will be generated. If the robot with the specified name is already under the control
     * of operator, the {@code RobotAlreadyExistException} will be thrown.
     *
     * @param type type of the robot
     * @param name name of the robot
     * @return a new robot
     * @throws RobotAlreadyExistException if the robot with the specified name is
     *                                    already under the control of operator
     */
    Robot createRobot(RobotType type, String name);

    /**
     * Returns the number of robots of the specified type under the control of operator.
     *
     * @param type type of the robot
     * @return the number of robots of the specified type under the control of operator
     */
    int numberRobots(RobotType type);

    /**
     * Returns the number of all robots under the control of operator.
     *
     * @return the number of robots of the specified type under the control of operator
     */
    int numberRobots();

    /**
     * Returns a robot with the specified name or {@code null} if this operator no control
     * a robot with this name.
     *
     * @param robotName name of the robot
     * @return a robot with the specified name
     * @throws RobotNameNotSpecifiedException if robot name is null or empty
     */
    Robot get(String robotName);

    /**
     * Assigns the specified task to the robot with the specified name and returns confirmation.
     *
     * @param task the assigned task
     * @param robotName name of the robot
     * @return <code>true</code> if the task is assigned and <code>false</code> otherwise
     * @throws TaskIsNullException if the task is {@code null}
     * @throws RobotNotFoundException if the robot with the specified name is not found
     * @throws RobotDeadException if the robot with the specified name is dead
     */
    boolean assignTask(Task task, String robotName);

    /**
     * Broadcasts about the need to perform the specified task and returns <code>true</code>
     * if the task is assigned. Any robot of the appropriate type (capable of doing it) should
     * begin to perform it. If a robot necessary type don't exist (and tracker is not installed
     * to control them) the task will hang in the queue until the required robot will be created.
     *
     * @param task the assigned task
     * @return <code>true</code> if the task is assigned and <code>false</code> otherwise
     * @throws TaskIsNullException if the specified task is null
     */
    boolean broadcastTask(Task task);

}
