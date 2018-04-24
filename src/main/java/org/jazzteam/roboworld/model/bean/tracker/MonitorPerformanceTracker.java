package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.exception.Constants;
import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.output.OutputWriter;
import org.jazzteam.roboworld.output.RoboWorldEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * This tracker monitors how the robots manage to perform their tasks. If it sees that robots for a specified
 * period of time have more tasks than the number of robots able to solve them, then it creates a new robot
 * appropriate type.
 */
public class MonitorPerformanceTracker extends TimeTracker {
    /** The map storing monitors performing a test of the performance of each type of robot separately */
    private final Map<RobotType, MonitorPerformance> timerTasks = new EnumMap<>(RobotType.class);
    /** The operator who uses this tracker and provides information about the robots */
    private final Operator operator;

    /**
     * The constructor defines the operator that will use this tracker and provide information about
     * the robots, as well as the period with which the tracker will monitor the robots.
     *
     * @param operator the operator that will use this tracker and provide information about the robots
     * @param period the period with which the tracker will monitor the robots
     * @throws NullPointerException if the specified operator is null
     * @throws IllegalArgumentException if the specified period has a negative or zero value
     */
    public MonitorPerformanceTracker(Operator operator, long period) {
        super(period);
        if(operator == null){
            throw new NullPointerException(Constants.OPERATOR_IS_NULL);
        }
        this.operator = operator;
    }

    /**
     * Creates a monitor to check the robots performance and starts it.
     *
     * @param type robot type
     * @throws RobotTypeNotSpecifiedException if the robot type is null
     */
    public void control(RobotType type){
        getTimer().purge();
        MonitorPerformance monitor = new MonitorPerformance(type);
        startMonitor(type, monitor);
    }

    /**
     * Saves the monitor in the {@code timerTasks} according to the robot type and schedules
     * the check with the installed period.
     *
     * @param type robot type
     * @param newMonitor new monitor to be installed for the specified type
     */
    private synchronized void startMonitor(RobotType type, MonitorPerformance newMonitor){
        MonitorPerformance oldMonitor = timerTasks.get(type);
        if(oldMonitor != null){
            oldMonitor.cancel();
        }
        timerTasks.put(type, newMonitor);
        getTimer().schedule(newMonitor, getPeriod(), getPeriod());
    }

    /**
     * This class extends {@code TimeTask} and implements the test of robot performance.
     */
    private class MonitorPerformance extends TimerTask {
        /** type of robot that is controlled */
        private final RobotType type;

        /**
         * This constructor initializes type of the robot.
         *
         * @param type the robot type
         * @throws NullPointerException if robot type is null
         */
        private MonitorPerformance(RobotType type){
            if(type == null){
                throw new RobotTypeNotSpecifiedException(Constants.TASK_TYPE_IS_NULL);
            }
            this.type = type;
        }

        /**
         * This method compares the number of tasks and the number of robots able to perform this task.
         * If the number of tasks more than the number of robots it creates a new robot of the appropriate type.
         * General tasks can be performed by any robot, so it takes their total quantity. If the number of tasks
         * less or equal the number of robots it stops working.
         */
        public void run(){
            int numberTasks = SharedBoard.getInstance().numberTasks(type);
            int numberRobots;
            if(type == RobotType.GENERAL){
                numberRobots = operator.numberRobots();
            } else {
                numberRobots = operator.numberRobots(type);
            }
            if(numberTasks > numberRobots){
                Robot robot = operator.createRobot(type);
                String message = type.name() + " robots can not cope, created an additional robot \"" + robot.getName() + "\"";
                OutputWriter.write(message, RoboWorldEvent.ROBOT);
            } else {
                this.cancel();
            }
        }
    }

}
