package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.tracker.MonitorPerformanceTracker;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.jazzteam.roboworld.model.facroty.RobotType;

public class MonitoringPerformanceOperator extends RecreaterOperator {
    private Tracker tracker;

    public MonitoringPerformanceOperator(long period){
        tracker = new MonitorPerformanceTracker(this, period);
    }

    public MonitoringPerformanceOperator(boolean recreateOfRobot, long period){
        this(period);
        setRecreateRobot(recreateOfRobot);
    }

    public void broadcastTask(Task task, RobotType type){
        super.broadcastTask(task, type);
        tracker.control(type);
    }

}
