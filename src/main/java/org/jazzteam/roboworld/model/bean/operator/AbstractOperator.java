package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractOperator implements Operator {
    private final Map<String, Robot> robots = new ConcurrentHashMap<>();

    protected Map<String, Robot> getRobots(){
        return robots;
    }

    public int countRobots(){
        return robots.size();
    }

    public int countRobots(RobotType type){
        final int[] count = new int[1];
        robots.forEach((name, robot) -> {
            if(robot.getRobotType() == type){
                count[0]++;
            }
        });
        return count[0];
    }

}
