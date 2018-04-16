package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.facroty.RobotType;

public class GeneralRobot extends AbstractRobot {

    public RobotType getRobotType(){
        return RobotType.GENERAL;
    }
}
