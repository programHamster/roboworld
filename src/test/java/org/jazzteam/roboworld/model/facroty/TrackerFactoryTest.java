package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedTrackerException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.RecreaterOperator;
import org.jazzteam.roboworld.model.bean.tracker.MonitorPerformanceTracker;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class TrackerFactoryTest {
    private static final Operator OPERATOR = new RecreaterOperator();
    private static final String PERIOD = "1";

    @DataPoints
    public static Object[][] trackerNames = new Object[][]{
            {Constants.INIT_PARAM_VALUE_TRACKER_PERFORMANCE, MonitorPerformanceTracker.class}
    };

    @Theory
    public void getTrackerFromFactory(Object... trackerNames) throws UnsupportedTrackerException {
        // act
        Tracker tracker = TrackerFactory.getTrackerFromFactory((String)trackerNames[0], PERIOD, OPERATOR);
        // assert
        assertEquals(tracker.getClass(), trackerNames[1]);
    }

    @Test(expected = UnsupportedTrackerException.class)
    public void getTrackerFromFactory_wrongParameter() throws UnsupportedTrackerException {
        // assert
        TrackerFactory.getTrackerFromFactory
                (org.jazzteam.roboworld.model.facroty.Constants.WRONG_PARAMETER, "", OPERATOR);
    }

    @Test(expected = NumberFormatException.class)
    public void getTrackerFromFactory_wrongAdditionalParameter() throws UnsupportedTrackerException {
        // assert
        TrackerFactory.getTrackerFromFactory(Constants.INIT_PARAM_VALUE_TRACKER_PERFORMANCE, "", OPERATOR);
    }

    @Test(expected = NullPointerException.class)
    public void getTrackerFromFactory_null() throws UnsupportedTrackerException {
        // assert
        TrackerFactory.getTrackerFromFactory(null, "", OPERATOR);
    }

}