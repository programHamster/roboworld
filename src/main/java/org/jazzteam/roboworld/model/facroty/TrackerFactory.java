package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedTrackerException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.tracker.MonitorPerformanceTracker;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;

public abstract class TrackerFactory {
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

    private static Tracker getMonitorPerformanceTracker(String additionalParam, Operator operator){
        long period;
        if(additionalParam != null){
            period = Long.valueOf(additionalParam);
        } else {
            period = Long.valueOf(Constants.INIT_PARAM_VALUE_TRACKER_DEFAULT_PERIOD);
        }
        return new MonitorPerformanceTracker(operator, period);
    }

}
