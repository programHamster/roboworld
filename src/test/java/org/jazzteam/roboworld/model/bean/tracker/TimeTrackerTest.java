package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTrackerTest {

    @Test
    public void testConstructor_positivePeriod() {
        new TimeTracker(1) {
            public void control(RobotType type) {}
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroPeriod() {
        new TimeTracker(0) {
            public void control(RobotType type) {}
        };
    }

    @Test
    public void setPeriod_positivePeriod() {
        TimeTracker tracker = new TimeTracker(1) {
            public void control(RobotType type) {}
        };
        tracker.setPeriod(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPeriod_zeroPeriod() {
        TimeTracker tracker = new TimeTracker(1) {
            public void control(RobotType type) {}
        };
        tracker.setPeriod(0);
    }
}