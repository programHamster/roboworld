package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.*;
import org.jazzteam.roboworld.model.bean.tracker.TrackerInitiator;
import org.jazzteam.roboworld.output.OutputInformation;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.output.RoboWorldEvent;

import java.util.UUID;

/**
 * This is operator capable of recreating robots in the event of their death.
 * This ability is enabled by default.
 */
public class RecreaterOperator extends AbstractOperator {
    /** the flag responsible for the recreating of the robots */
    private boolean recreate = true;

    /**
     * This is default constructor.
     */
    public RecreaterOperator(){
    }

    /**
     * This constructor sets the ability to recreate robots.
     *
     * @param recreateOfRobot pass <code>false</code> for turn off the recreate
     */
    public RecreaterOperator(boolean recreateOfRobot){
        this.recreate = recreateOfRobot;
    }

    /**
     * Returns the mode of recreating robots.
     *
     * @return the mode of recreating robots
     */
    public boolean isRecreateRobot() {
        return recreate;
    }

    /**
     * Sets the mode of recreating robots.
     *
     * @param recreateRobot the flag responsible for the recreating of the robots
     */
    public void setRecreateRobot(boolean recreateRobot) {
        this.recreate = recreateRobot;
    }

    /**
     * Creates and returns a new robot of the specified type. The created robot will
     * be started immediately. The name is set according to the specified. If the
     * specified name is {@code null} or empty, a unique random name will be generated.
     * If the robot with the specified name is already under the control of operator, the
     * {@code RobotAlreadyExistException} will be thrown.
     *
     * @param type type of the robot
     * @param name name of the robot
     * @return a new robot
     * @throws RobotAlreadyExistException if the robot with the specified name is
     *                                    already under the control of operator
     */
    public Robot createRobot(RobotType type, String name) {
        Robot robot;
        if(name == null || (name = name.trim()).isEmpty()){
            robot = createRobot(type);
        } else {
            robot = createRobot(type, name, false);
        }
        return robot;
    }

    /**
     * Creates and returns a new robot of the specified type. The created robot will
     * be started immediately. The name for a new robot will be generated automatically.
     *
     * @param type type of the robot
     * @return a new robot
     */
    public Robot createRobot(RobotType type) {
        Robot robot;
        try {
            robot = createRobot(type, getRandomName(), false);
        } catch (RobotAlreadyExistException e) {
            // if the names matched try again
            robot = createRobot(type);
        }
        return robot;
    }

    /**
     * this is a common method for creating robots. If the flag <code>withReplacement</code>
     * is <code>false</code> and a robot with the same name already exists, then the
     * {@code RobotAlreadyExistException} will be thrown.
     *
     * @param type type of the robot
     * @param name robot name
     * @param withReplacement if pass <code>true</code>, then if a robot with the same name
     *                       already exists, it will be replaced. if pass <code>false</code>,
     *                       then if a robot with the same name already exists, an exception
     *                       will be thrown
     * @return {@code null} if operator does not have a robot with the specified name and if
     *                      the robot with this name exists, the old robot will be returned
     * @throws RobotAlreadyExistException if robot with the specified name exists and flag
     *                                      <code>withReplacement</code> is <code>false</code>
     */
    private Robot createRobot(RobotType type, String name, boolean withReplacement) {
        if(type == null){
            throw new NullPointerException(Constants.ROBOT_TYPE_IS_NULL);
        }
        Robot robot = type.getRobot();
        if(name != null){
            robot.setName(name);
        }
        robot.start();
        if(withReplacement){
            put(robot);
        } else {
            if(putIfAbsent(robot) != null){
                throw new RobotAlreadyExistException(robot);
            }
        }
        return robot;
    }

    /**
     * Broadcasts about the need to perform the specified task and returns <code>true</code>
     * if the task is assigned. Any robot of the appropriate type (capable of doing it) should
     * begin to perform it. If a robot necessary type don't exist (and tracker is not installed
     * to control them) the task will hang in the queue until the required robot will be created.
     *
     * Also in this method, the operator outputs robots from sleep mode and recreates them as
     * needed. The tracker also come in to action.
     *
     * @param task the assigned task
     * @return <code>true</code> if the task is assigned and <code>false</code> otherwise
     * @throws TaskIsNullException if the specified task is null
     */
    public boolean broadcastTask(Task task){
        boolean success = SharedBoard.getInstance().put(task);
        getRobots().forEach((name, robot) -> {
            try{
                robot.wakeUp();
            } catch(RobotDeadException e){
                if(recreate){
                    createRobot(robot.getRobotType(), robot.getName(), true);
                    OutputInformation.write("Robot \"" + robot.getName() + "\" is recreated");
                } else {
                    remove(robot.getName());
                    OutputInformation.write("Robot \"" + robot.getName() + "\" is removed", RoboWorldEvent.ROBOT);
                }
            }
        });
        RobotType type = RobotType.identifyRobotType(task);
        getTrackerInitiator().control(new BroadcastEvent(this, type));
        return success;
    }

    /**
     * Assigns the specified task to the robot with the specified name and returns confirmation.
     *
     * @param task the assigned task
     * @param robotName name of the robot
     * @return <code>true</code> if the task is assigned and <code>false</code> otherwise
     * @throws TaskIsNullException if the task is {@code null}
     * @throws RobotNotFoundException if the robot with the specified name is not found
     * @throws RobotDeadException if the robot with the specified name is dead
     *                              and the recreate mode is turn off
     */
    public boolean assignTask(Task task, String robotName){
        if(task == null){
            throw new TaskIsNullException();
        }
        Robot robot = get(robotName);
        if(robot == null){
            throw new RobotNotFoundException(robotName);
        }
        if(!tryAssignTask(task, robot)){
            throw new RobotDeadException(robot);
        }
        return true;
    }

    /**
     * This method tries to assign a task to a robot and, if necessary, recreate it. If the recreate mod
     * is off, the robot will be removed and returns <code>false</code>.
     *
     * @param task assigned task
     * @param robot the robot to which the task is assigned
     * @return <code>true</code> if the task is assigned and <code>false</code> if robot is dead
     */
    private boolean tryAssignTask(Task task, Robot robot){
        boolean success = false;
        try{
            if(robot.isDie()){
                if(recreate){
                    try{
                        robot = createRobot(robot.getRobotType(), robot.getName(), true);
                        OutputInformation.write("Robot \"" + robot.getName() + "\" is recreated");
                    } catch(RobotAlreadyExistException e){
                        // never happen
                    }
                } else {
                    remove(robot.getName());
                    return false;
                }
            }
            robot.addTask(task);
            robot.wakeUp();
            success = true;
        } catch(RobotDeadException e){
            tryAssignTask(task, robot);
        }
        return success;
    }

    /**
     * Returns the generated random name. Its length is @{code LENGTH_NAME}.
     *
     * @return the generated random name
     */
    private static String getRandomName(){
        final int LENGTH_NAME = 5;
        String uuid = UUID.randomUUID().toString().replaceAll(org.jazzteam.roboworld.
                Constants.DEFAULT_UUID_DELIMITER, org.jazzteam.roboworld.Constants.EMPTY);
        return uuid.substring(0, LENGTH_NAME);
    }

}
