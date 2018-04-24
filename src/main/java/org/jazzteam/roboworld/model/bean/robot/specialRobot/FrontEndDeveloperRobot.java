package org.jazzteam.roboworld.model.bean.robot.specialRobot;

import org.jazzteam.roboworld.model.bean.robot.AbstractSpecialRobot;
import org.jazzteam.roboworld.model.facroty.RobotType;

/**
 * The robot is trained to perform front-end tasks
 */
public class FrontEndDeveloperRobot extends AbstractSpecialRobot {

    public RobotType getRobotType(){
        return RobotType.FRONT_END_DEVELOPER;
    }

}
