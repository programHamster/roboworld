package by.roboworld.model.bean.operator;

import by.roboworld.exception.Constants;
import by.roboworld.exception.RobotAlreadyExistException;
import by.roboworld.exception.RobotDeadException;
import by.roboworld.exception.RobotNotFoundException;
import by.roboworld.model.bean.board.SharedBoard;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.facroty.RobotType;
import by.roboworld.output.OutputInformation;
import by.roboworld.output.RoboworldEvent;

import java.util.Objects;
import java.util.UUID;

/**
 * This is operator capable of recreating robots in the event of their death.
 * This ability is enabled by default.
 */
public class RecreaterOperator extends AbstractOperator {
    /** The flag responsible for the recreating of the robots. */
    private boolean recreate = true;

    /**
     * This is default constructor.
     */
    public RecreaterOperator() {
    }

    /**
     * This constructor sets the ability to recreate robots.
     *
     * @param recreateOfRobot pass <code>false</code> for turn off the recreate
     */
    public RecreaterOperator(boolean recreateOfRobot) {
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
     * @param recreateRobot the flag responsible for the recreating of the
     *                     robots
     */
    public void setRecreateRobot(boolean recreateRobot) {
        this.recreate = recreateRobot;
    }

    /**
     * Creates and returns a new robot of the specified type. The created robot
     * will be started immediately. The name is set according to the specified.
     * If the specified name is {@code null} or empty, a unique random name
     * will be generated. If the robot with the specified name is already under
     * the control of operator, the {@code RobotAlreadyExistException} will be
     * thrown.
     *
     * @param type type of the robot
     * @param name name of the robot
     * @return a new robot
     * @throws RobotAlreadyExistException if the robot with the specified name
     *                                 is already under the control of operator
     */
    public Robot createRobot(final RobotType type, String name) {
        Robot robot;
        if (name == null || (name = name.trim()).isEmpty()) {
            robot = createRobot(type);
        } else {
            robot = createRobot(type, name, false);
        }
        return robot;
    }

    /**
     * Creates and returns a new robot of the specified type. The created robot
     * will be started immediately. The name for a new robot will be generated
     * automatically.
     *
     * @param type type of the robot
     * @return a new robot
     */
    public Robot createRobot(final RobotType type) {
        String robotName;
        do {
            robotName = getRandomName();
        } while (get(robotName) != null);
        Robot robot;
        try {
            robot = createRobot(type, robotName, false);
        } catch (RobotAlreadyExistException e) {
            // if the names matched try again
            robot = createRobot(type);
        }
        return robot;
    }

    /**
     * this is a common method for creating robots. If the flag
     * <code>withReplacement</code> is <code>false</code> and a robot with the
     * same name already exists, then the {@code RobotAlreadyExistException}
     * will be thrown.
     *
     * @param type type of the robot
     * @param name robot name
     * @param withReplacement if pass <code>true</code>, then if a robot with
     *                       the same name already exists, it will be replaced.
     *                       if pass <code>false</code>, then if a robot with
     *                       the same name already exists, an exception will be
     *                       thrown
     * @return {@code null} if operator does not have a robot with the
     *         specified name and if the robot with this name exists, the old
     *         robot will be returned
     * @throws RobotAlreadyExistException if robot with the specified name
     *                             exists and flag <code>withReplacement</code>
     *                             is <code>false</code>
     */
    private Robot createRobot(final RobotType type, final String name, final boolean withReplacement) {
        Objects.requireNonNull(type, Constants.ROBOT_TYPE_IS_NULL);
        Robot robot = type.getRobot();
        if (name != null) {
            robot.setName(name);
        }
        robot.start();
        if (withReplacement) {
            put(robot);
        } else {
            if (putIfAbsent(robot) != null) {
                throw new RobotAlreadyExistException(robot);
            }
        }
        return robot;
    }

    /**
     * Broadcasts about the need to perform the specified task and returns
     * <code>true</code> if the task is assigned. Any robot of the appropriate
     * type (capable of doing it) should begin to perform it. If a robot
     * necessary type don't exist (and tracker is not installed to control
     * them) the task will hang in the queue until the required robot will be
     * created.
     *
     * Also in this method, the operator outputs robots from sleep mode and
     * recreates them as needed. The tracker also come in to action.
     *
     * @param task the assigned task
     * @return <code>true</code> if the task is assigned and <code>false</code>
     *         otherwise
     * @throws NullPointerException if the specified task is null
     */
    public boolean broadcastTask(Task task) {
        boolean success = SharedBoard.getInstance().put(task);
        getRobots().forEach((name, robot) -> {
            try {
                robot.wakeUp();
            } catch (RobotDeadException e) {
                if (recreate) {
                    createRobot(robot.getRobotType(), robot.getName(), true);
                    OutputInformation.write("Robot \"" + robot.getName() +
                            "\" is recreated");
                } else {
                    remove(robot.getName());
                    OutputInformation.write("Robot \"" + robot.getName() +
                            "\" is removed", RoboworldEvent.ROBOT);
                }
            }
        });
        RobotType type = RobotType.identifyRobotType(task);
        if (getTrackerInitiator() != null) {
            getTrackerInitiator().control(new BroadcastEvent(this, type));
        }
        return success;
    }

    /**
     * Assigns the specified task to the robot with the specified name.
     *
     * @param task the assigned task
     * @param robotName name of the robot
     * @throws NullPointerException if the task is {@code null}
     * @throws RobotNotFoundException if the robot with the specified name is
     *                                not found
     * @throws RobotDeadException if the robot with the specified name is dead
     *                              and the recreate mode is turn off
     */
    public void assignTask(Task task, String robotName) {
        Objects.requireNonNull(task, Constants.TASK_IS_NULL);
        Robot robot = get(robotName);
        if (robot == null) {
            throw new RobotNotFoundException(robotName);
        }
        if (!tryAssignTask(task, robot)) {
            throw new RobotDeadException(robot);
        }
    }

    /**
     * This method tries to assign a task to a robot and, if necessary,
     * recreate it. If the recreate mod is off, the robot will be removed
     * and returns <code>false</code>.
     *
     * @param task assigned task
     * @param robot the robot to which the task is assigned
     * @return <code>true</code> if the task is assigned and <code>false</code>
     *         if robot is dead
     */
    private boolean tryAssignTask(Task task, Robot robot) {
        boolean success = false;
        try {
            if (robot.isDie()) {
                if (recreate) {
                    try {
                        robot = createRobot(robot.getRobotType(), robot.getName(), true);
                        OutputInformation.write("Robot \"" + robot.getName() +
                                "\" is recreated");
                    } catch (RobotAlreadyExistException e) {
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
        } catch (RobotDeadException e) {
            tryAssignTask(task, robot);
        }
        return success;
    }

    /**
     * Returns the generated random name. Its length is @{code LENGTH_NAME}.
     *
     * @return the generated random name
     */
    private static String getRandomName() {
        final int lengthName = 5;
        String uuid = UUID.randomUUID().toString().replaceAll
                (by.roboworld.Constants.DEFAULT_UUID_DELIMITER, by.roboworld.Constants.EMPTY);
        return uuid.substring(0, lengthName);
    }

}
