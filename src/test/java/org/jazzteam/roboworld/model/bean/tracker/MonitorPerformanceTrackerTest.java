package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.RecreaterOperator;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class MonitorPerformanceTrackerTest {
    private final Operator operator = new RecreaterOperator(true);
    private final Tracker tracker = new MonitorPerformanceTracker(operator, 1);
    {
        operator.addTracker(tracker);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_null(){
        // act
        new MonitorPerformanceTracker(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void control_null(){
        // arrange
        Tracker tracker = new MonitorPerformanceTracker(operator, 1);
        // act
        tracker.control(null);
    }

    @Test(timeout = 1000)
    public void control() {
        // arrange
        RobotType testType = RobotType.BACK_END_DEVELOPER;
        operator.createRobot(testType);
        int numberRobotsBefore = operator.countRobots(testType);
        Task task = new BackEndTask();
        // act
        for(int i=0; i<3; i++){
            operator.broadcastTask(task);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int numberRobotsAfter = operator.countRobots(testType);
        // assert
        assertTrue(numberRobotsAfter > numberRobotsBefore);
    }
}