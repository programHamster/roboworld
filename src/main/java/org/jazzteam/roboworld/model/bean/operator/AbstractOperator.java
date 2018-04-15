package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.robot.Robot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractOperator implements Operator {
    private final Map<String, Robot> robots = new ConcurrentHashMap<>();

    protected Map<String, Robot> getRobots(){
        return robots;
    }

}
