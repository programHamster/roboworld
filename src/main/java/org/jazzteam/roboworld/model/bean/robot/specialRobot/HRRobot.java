package org.jazzteam.roboworld.model.bean.robot.specialRobot;

import org.jazzteam.roboworld.model.bean.robot.AbstractSpecialRobot;
import org.jazzteam.roboworld.model.facroty.RobotType;

/**
 * The robot is trained to perform HR tasks
 */
public class HRRobot extends AbstractSpecialRobot {

    public RobotType getRobotType(){
        return RobotType.HR;
    }

}
