package by.roboworld.model.bean.operator;

import by.roboworld.exception.Constants;
import by.roboworld.exception.notSpecified.RobotNameNotSpecifiedException;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.bean.tracker.TrackerInitiator;
import by.roboworld.model.facroty.RobotType;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This implementation of the Operator based on the {@code ConcurrentHashMap}
 * because it hash table in which all operations are thread-safe and retrieval
 * operations such as {@code get()} generally do not block.
 *
 */
public abstract class AbstractOperator implements Operator {

    /** The storage map for robots. */
    private final Map<String, Robot> robots = new ConcurrentHashMap<>();

    /** Used for control a broadcasting by trackers. */
    private TrackerInitiator trackerInitiator;

    /**
     * Saves the robot in the map using the robot name as the key. If the robot
     * with this name exists, the old robot will be returned and replaced by
     * the specified robot. If the robot with the specified name is not stored,
     * {@code null} is returned.
     *
     * @param robot saved robot
     * @return if the robot with this name exists, the old robot will be
     *         returned and {@code null} if the specified robot is a new robot
     * @throws NullPointerException if the specified robot is {@code null}
     */
    protected final Robot put(final Robot robot) {
        Objects.requireNonNull(robot, Constants.ROBOT_IS_NULL);
        return robots.put(robot.getName(), robot);
    }

    /**
     * Saves the robot in the map using the robot name as the key. If the robot
     * with this name exists, the old robot will be returned and the specified
     * robot won't be saved. If the robot with the specified name is not stored
     * it will be saved and returned {@code null}.
     *
     * @param robot saved robot
     * @return returns {@code null} if the specified robot is new for this
     *                      operator and returns the old robot if robot with
     *                      this name already exists
     * @throws NullPointerException if the specified robot is {@code null}
     */
    protected final Robot putIfAbsent(final Robot robot) {
        Objects.requireNonNull(robot, Constants.ROBOT_IS_NULL);
        return robots.putIfAbsent(robot.getName(), robot);
    }

    /**
     * Returns a robot with the specified name or {@code null} if this operator
     * no control a robot with this name.
     *
     * @param robotName name of the robot
     * @return a robot with the specified name
     * @throws RobotNameNotSpecifiedException if robot name is empty
     */
    public final Robot get(final String robotName) {
        Objects.requireNonNull(robotName, Constants.ROBOT_NAME_IS_NULL);
        if (robotName.isEmpty()) {
            throw new RobotNameNotSpecifiedException();
        }
        return robots.get(robotName);
    }

    /**
     * Removes and returns the robot with the specified name.
     *
     * @param robotName name of the robot
     * @return the robot who was removed
     * @throws RobotNameNotSpecifiedException if robot name is null or empty
     */
    protected final Robot remove(final String robotName) {
        Objects.requireNonNull(robotName, Constants.ROBOT_NAME_IS_NULL);
        if (robotName.isEmpty()) {
            throw new RobotNameNotSpecifiedException();
        }
        return robots.remove(robotName);
    }

    /**
     * Returns the number of all robots under the control of operator.
     *
     * @return the number of robots of the specified type under the control of
     *          operator
     */
    public int numberRobots() {
        return robots.size();
    }

    /**
     * Returns the number of robots of the specified type under the control of
     * operator.
     *
     * @param type type of the robot
     * @return the number of robots of the specified type under the control of
     *          operator
     */
    public int numberRobots(final RobotType type) {
        return (int) robots.entrySet().stream()
                .filter(entry -> entry.getValue().getRobotType() == type)
                .count();
    }

    /**
     * Returns the {@code Map} contains all robots under the control of the
     * operator. If the operator hasn't robots, this method must returns an
     * empty {@code Map}. The returned {@code Map} is unmodifiable because only
     * the operator can change it.
     *
     * @return the {@code Map} contains all robots under the control of the
     *          operator
     */
    public Map<String, Robot> getRobots() {
        return Collections.unmodifiableMap(robots);
    }

    /**
     * Sets a TrackerInitiator.
     *
     * @param trackerInitiator trackerInitiator
     */
    public void setTrackerInitiator(final TrackerInitiator trackerInitiator) {
        this.trackerInitiator = trackerInitiator;
    }

    /**
     * Returns TrackerInitiator.
     *
     * @return trackerInitiator
     */
    public TrackerInitiator getTrackerInitiator() {
        return trackerInitiator;
    }
}
