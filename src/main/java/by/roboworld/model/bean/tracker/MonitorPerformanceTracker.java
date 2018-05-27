package by.roboworld.model.bean.tracker;

import by.roboworld.exception.Constants;
import by.roboworld.model.bean.board.SharedBoard;
import by.roboworld.model.bean.operator.BroadcastEvent;
import by.roboworld.model.bean.operator.Operator;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.facroty.RobotType;
import by.roboworld.output.OutputInformation;
import by.roboworld.output.RoboworldEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;

/**
 * This tracker monitors how the robots manage to perform their tasks. If it
 * sees that robots for a specified period of time have more tasks than the
 * number of robots able to solve them, then it creates a new robot appropriate
 * type.
 */
public class MonitorPerformanceTracker extends TimeTracker {
    /**
     * The map storing monitors performing a test of the performance of each
     * type of robot separately.
     */
    private final Map<RobotType, MonitorPerformance> timerTasks = new EnumMap<>(RobotType.class);

    /**
     * The constructor defines the operator that will use this tracker and
     * provide information about the robots, as well as the period with which
     * the tracker will monitor the robots.
     *
     * @param period the period with which the tracker will monitor the robots
     * @throws NullPointerException if the specified operator is null
     * @throws IllegalArgumentException if the specified period has a negative
     *                                  or zero value
     */
    public MonitorPerformanceTracker(long period) {
        super(period);
    }

    /**
     * Creates a monitor to check the robots performance and starts it.
     *
     * @param broadcast a broadcast event
     */
    @Override
    public void onApplicationEvent(BroadcastEvent broadcast) {
        getTimer().purge();
        RobotType type = broadcast.getType();
        MonitorPerformance monitor = new MonitorPerformance(broadcast.getSource(), type);
        startMonitor(type, monitor);
    }

    /**
     * Saves the monitor in the {@code timerTasks} according to the robot type
     * and schedules the check with the installed period.
     *
     * @param type robot type
     * @param newMonitor new monitor to be installed for the specified type
     */
    private synchronized void startMonitor(RobotType type, MonitorPerformance newMonitor) {
        MonitorPerformance oldMonitor = timerTasks.get(type);
        if (oldMonitor != null) {
            oldMonitor.cancel();
        }
        timerTasks.put(type, newMonitor);
        getTimer().schedule(newMonitor, getPeriod(), getPeriod());
    }

    /**
     * This class extends {@code TimeTask} and implements the test of robot
     * performance.
     */
    private class MonitorPerformance extends TimerTask {
        /** Type of robot that is controlled. */
        private final RobotType type;
        /**
         * The operator who uses this tracker and provides information
         * about the robots.
         */
        private final Operator operator;

        /**
         * This constructor initializes type of the robot.
         *
         * @param operator operator that raised the event
         * @param type the robot type
         * @throws NullPointerException if robot type is null
         */
        private MonitorPerformance(Operator operator, RobotType type) {
            Objects.requireNonNull(operator, "Operator is null");
            Objects.requireNonNull(type, Constants.TASK_TYPE_IS_NULL);
            this.operator = operator;
            this.type = type;
        }

        /**
         * This method compares the number of tasks and the number of robots
         * able to perform this task. If the number of tasks more than the
         * number of robots it creates a new robot of the appropriate type.
         * General tasks can be performed by any robot, so it takes their total
         * quantity. If the number of tasks less or equal the number of robots
         * it stops working.
         */
        public void run() {
            int numberTasks = SharedBoard.getInstance().numberTasks(type);
            int numberRobots;
            if (type == RobotType.GENERAL) {
                numberRobots = operator.numberRobots();
            } else {
                numberRobots = operator.numberRobots(type);
            }
            if (numberTasks > numberRobots) {
                Robot robot = operator.createRobot(type);
                String message = type.name() +
                        " robots can not cope, created an additional robot \""
                        + robot.getName() + "\"";
                OutputInformation.write(message, RoboworldEvent.ROBOT);
            } else {
                this.cancel();
            }
        }
    }

}
