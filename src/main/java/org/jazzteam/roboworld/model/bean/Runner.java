package org.jazzteam.roboworld.model.bean;

import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.RobotType;
import org.jazzteam.roboworld.model.bean.task.DieTask;
import org.jazzteam.roboworld.model.bean.task.TalkerTask;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.tracker.ActivityTracker;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;

public class Runner {
    public static void main(String[] args) {
        Operator operator = new Operator();
        try {
            operator.createRobot(RobotType.DOCILE, "robot1");
            operator.createRobot(RobotType.DOCILE, "robot2");
            operator.createRobot(RobotType.DOCILE, "robot3");
        } catch (RobotAlreadyExistException e) {
            e.printStackTrace();
        }

        Task task = new TalkerTask("Hi");
        Task task2 = new TalkerTask("Hello");
        operator.broadcastTask(task);
        operator.assignTask(task2, "robot1");
        operator.broadcastTask(task);
        operator.broadcastTask(task);
        operator.broadcastTask(task);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        operator.assignTask(new DieTask(), "robot1");
        operator.broadcastTask(task2);
//        operator.assignTask(task, "robot1");
    }
}
