package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedTrackerException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.tracker.MonitorPerformanceTracker;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;

/**
 * This factory is used for produce of trackers by the specified name.
 */
public abstract class TrackerFactory {

    /**
     * Returns a particular implementation according to the specified name.
     *
     * Each implementation of an tracker has a special parameter for the constructor,
     * so parsing this parameter and defining the desired constructor are put in a separate
     * method for each implementation of the tracker.
     *
     * @param trackerName name of the tracker
     * @param additionalTrackerParam additional parameter for pass to constructor
     * @param operator the operator who uses tracker and provides information about the robots
     * @return a particular implementation according to the specified name
     * @throws UnsupportedTrackerException if the specified name of the tracker is wrong
     * @throws NullPointerException if the specified name of the tracker is <code>null</code>
     */
    public static Tracker getTrackerFromFactory(String trackerName, String additionalTrackerParam, Operator operator)
            throws UnsupportedTrackerException{
        if(trackerName == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.TRACKER_NAME_IS_NULL);
        }
        switch(trackerName){
            case Constants.INIT_PARAM_VALUE_TRACKER_PERFORMANCE:
                return getMonitorPerformanceTracker(additionalTrackerParam, operator);
            default:
                throw new UnsupportedTrackerException();
        }
    }

    /**
     * Returns an instance of the tracker created with passed the additional parameter.
     *
     * @param additionalParam additional parameter for pass to constructor
     * @param operator the operator who uses tracker and provides information about the robots
     * @return an instance of the tracker created with passed the additional parameter
     */
    private static Tracker getMonitorPerformanceTracker(String additionalParam, Operator operator){
        long period;
        if(additionalParam != null){
            period = Long.valueOf(additionalParam);
        } else {
            period = Long.valueOf(Constants.INIT_PARAM_VALUE_TRACKER_PERIOD);
        }
        return new MonitorPerformanceTracker(operator, period);
    }

}
