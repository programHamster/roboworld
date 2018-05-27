package by.roboworld.model.bean.robot.specialRobot;

import by.roboworld.model.bean.robot.AbstractSpecialRobot;
import by.roboworld.model.facroty.RobotType;

/**
 * The robot is trained to perform back-end tasks.
 */
public class BackEndDeveloperRobot extends AbstractSpecialRobot {

    public RobotType getRobotType() {
        return RobotType.BACK_END_DEVELOPER;
    }

}
