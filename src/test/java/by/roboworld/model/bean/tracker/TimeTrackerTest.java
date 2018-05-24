package by.roboworld.model.bean.tracker;

import by.roboworld.model.bean.operator.BroadcastEvent;
import org.junit.Test;

public class TimeTrackerTest {

    @Test
    public void testConstructor_positivePeriod() {
        new TimeTracker(1) {
            public void onApplicationEvent(BroadcastEvent broadcast) {}
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroPeriod() {
        new TimeTracker(0) {
            public void onApplicationEvent(BroadcastEvent broadcast) {}
        };
    }

}