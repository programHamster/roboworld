package by.roboworld.model.bean.robot.specialRobot;

import by.roboworld.model.bean.robot.AbstractSpecialRobot;
import by.roboworld.model.facroty.RobotType;

/**
 * The robot is trained to perform front-end tasks
 */
public class FrontEndDeveloperRobot extends AbstractSpecialRobot {

    public RobotType getRobotType(){
        return RobotType.FRONT_END_DEVELOPER;
    }

}
