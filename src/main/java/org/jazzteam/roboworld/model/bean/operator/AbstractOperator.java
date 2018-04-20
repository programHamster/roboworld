package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.exception.notSpecified.RobotNameNotSpecifiedException;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractOperator implements Operator {
    private final Map<String, Robot> robots = new ConcurrentHashMap<>();

    protected final Robot put(Robot robot){
        return robots.put(robot.getName(), robot);
    }

    protected final Robot putIfAbsent(Robot robot){
        return robots.putIfAbsent(robot.getName(), robot);
    }

    public final Robot get(String robotName){
        if(robotName == null || robotName.isEmpty()){
            throw new RobotNameNotSpecifiedException();
        }
        return robots.get(robotName);
    }

    protected final Robot remove(String robotName){
        return robots.remove(robotName);
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

    public Map<String, Robot> getRobots(){
        return Collections.unmodifiableMap(robots);
    }

}
